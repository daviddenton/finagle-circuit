package io.github.finaglecircuit

import java.time.Duration
import java.util.concurrent.TimeUnit._

import akka.actor.ActorSystem
import akka.pattern.CircuitBreakerOpenException
import com.twitter.finagle.FailedFastException
import com.twitter.util.Future
import io.github.finaglecircuit.util.FutureConversions

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration
import scala.language.implicitConversions

class AkkaCircuitBreaker(circuitConfig: CircuitConfig) extends CircuitBreaker {

  val name = circuitConfig.circuitName
  private val system = ActorSystem(name.value)
  private val breaker = new akka.pattern.CircuitBreaker(system.scheduler, circuitConfig.failLimit, circuitConfig.timeout, circuitConfig.downTime)

  override def onCircuitStatusChange(state: CircuitStateChange => Unit): CircuitBreaker = {
    breaker
      .onOpen(state(CircuitStateChange(name, CircuitStatus.Open)))
      .onHalfOpen(state(CircuitStateChange(name, CircuitStatus.HalfOpen)))
      .onClose(state(CircuitStateChange(name, CircuitStatus.Closed)))
    this
  }

  override def withCircuit[T](body: => Future[T]): Future[T] = {
    FutureConversions.asTwitter(breaker.withCircuitBreaker[T](FutureConversions.asScala(body))).rescue {
      case e: CircuitBreakerOpenException => Future.exception(new CircuitBroken(name, "Open"))
      case e: FailedFastException => Future.exception(new CircuitBroken(name, "Down"))
    }
  }

  private implicit def toScala(duration: Duration): FiniteDuration = FiniteDuration(duration.toMillis, MILLISECONDS)

  def stop() = system.shutdown()
}
