package io.github.daviddenton.finaglecircuit.circuit

import java.time.Duration

import com.twitter.finagle.FailedFastException
import com.twitter.util.{Await, Future, Try}
import io.github.daviddenton.finaglecircuit._
import org.scalatest.{FunSpec, ShouldMatchers}

import scala.collection.mutable.ListBuffer

/**
 * Yes, we realise that this test uses Thread.sleeps, which sucks - but we're at the behest of an internal timer in the
 * Akka circuit breaker client which does incremental backoff.
 */
class AkkaCircuitBreakerTest extends FunSpec with ShouldMatchers {

  private val name = CircuitName("mysystem")
  private val timeout = Duration.ofMillis(10)
  private val downTime = Duration.ofMillis(50)
  private val config = CircuitConfig(name, timeout, downTime, 3)


  describe("failed calls errors lifecycle") {
    val states = new ListBuffer[CircuitStateChange]()
    val circuitBreaker = new AkkaCircuitBreaker(config).onCircuitStatusChange(states += _)

    val e = new RuntimeException("poo")

    it("first failure is propagated to the caller") {
      Try(Await.result(circuitBreaker.withCircuit(Future.exception(e)))).throwable shouldEqual e
    }

    it("second failure is propagated to the caller") {
      Try(Await.result(circuitBreaker.withCircuit(Future.exception(e)))).throwable shouldEqual e
    }

    it("third failure is propagated to the caller") {
      Try(Await.result(circuitBreaker.withCircuit(Future.exception(e)))).throwable shouldEqual e
    }

    it("event is sent that the circuit is tripped on the third failure") {
      Thread.sleep(5)
      states.toList shouldEqual List(CircuitStateChange(name, CircuitStatus.Open))
      states.clear()
    }

    it("circuit is now has tripped - service is unavailable") {
      Try(respondWith(circuitBreaker, 0)).throwable shouldEqual CircuitBroken(name, "Open")
    }

    it("the circuit is half opened after the timeout") {
      Thread.sleep(downTime.toMillis + 1000)

      states.toList shouldEqual List(CircuitStateChange(name, CircuitStatus.HalfOpen))
      states.clear()

      respondWith(circuitBreaker, 0) shouldEqual "yay"
    }
    it("the circuit is closed") {
      Thread.sleep(downTime.toMillis)
      states.toList shouldEqual List(CircuitStateChange(name, CircuitStatus.Closed))
    }
  }

  describe("slow responses lifecycle") {
    val states = new ListBuffer[CircuitStateChange]()
    val circuitBreaker = new AkkaCircuitBreaker(config).onCircuitStatusChange(states += _)

    it("first slow response is propagated to the caller (but counts as a failure to the circuit") {
      respondWith(circuitBreaker, 50) shouldEqual "yay"
    }
    it("second slow response is propagated to the caller (but counts as a failure to the circuit") {
      respondWith(circuitBreaker, 50) shouldEqual "yay"
    }
    it("third slow response is propagated to the caller (but counts as a failure to the circuit") {
      respondWith(circuitBreaker, 50) shouldEqual "yay"
    }
    it("event is sent that the circuit is tripped on the third failure") {
      Thread.sleep(5)
      states.toList shouldEqual List(CircuitStateChange(name, CircuitStatus.Open))
      states.clear()
    }

    it("circuit is now has tripped - service is unavailable") {
      Try(respondWith(circuitBreaker, 0)).throwable shouldEqual CircuitBroken(name, "Open")
    }
    it("the circuit is half opened after the timeout") {
      Thread.sleep(downTime.toMillis + 20)
      states.toList shouldEqual List(CircuitStateChange(name, CircuitStatus.HalfOpen))
      states.clear()
      respondWith(circuitBreaker, 0) shouldEqual "yay"
    }
    it("the circuit is closed") {
      states.toList shouldEqual List(CircuitStateChange(name, CircuitStatus.Closed))
    }
  }

  private def respondWith(circuitBreaker: CircuitBreaker, latency: Int): String = {
    Await.result(circuitBreaker.withCircuit({
      Thread.sleep(latency)
      Future.value("yay")
    }))
  }

  describe("fail fast - unexpected errors") {
    val states = new ListBuffer[CircuitStateChange]()
    val circuitBreaker = new AkkaCircuitBreaker(config).onCircuitStatusChange(states += _)
    val e = new scala.RuntimeException()

    it("Fail fast errors are converted into Circuit Down") {
      Try(Await.result(circuitBreaker.withCircuit(throw new FailedFastException("")))).throwable shouldEqual CircuitBroken(name, "Down")
    }
  }
}
