package io.github.finaglecircuit.test

import com.twitter.util.{Await, Future}
import io.github.finaglecircuit.{CircuitStatus, CircuitStateChange, CircuitName}
import org.scalatest.{FunSpec, ShouldMatchers}

class TestingCircuitBreakerTest extends FunSpec with ShouldMatchers {

  describe("TestingCircuitBreaker") {
    val name = CircuitName("circuit")
    val breaker = new TestingCircuitBreaker(name)

    it("calls through and returns result") {
      Await.result(breaker.withCircuit(Future.value("bob"))) shouldEqual "bob"
      breaker.stop()
    }

    it("can force through a state-change callback") {
      var state = Option.empty[CircuitStateChange]
      breaker.onCircuitStatusChange(s => state = Some(s))
      val newState = CircuitStateChange(name, CircuitStatus.HalfOpen)
      breaker.changeState(newState)
      state shouldEqual Some(newState)
    }
  }

}
