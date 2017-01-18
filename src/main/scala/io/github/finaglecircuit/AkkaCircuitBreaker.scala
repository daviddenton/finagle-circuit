package io.github.finaglecircuit

import java.time.Duration
import java.util.concurrent.TimeUnit._
import java.util.concurrent.TimeoutException

import akka.actor.ActorSystem
import akka.pattern.CircuitBreakerOpenException
import com.twitter.bijection.twitter_util.UtilBijections
import com.twitter.finagle.FailedFastException
import com.twitter.util.Future

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration
import scala.language.implicitConversions

class AkkaCircuitBreaker(circuitConfig: CircuitConfig) extends CircuitBreaker {

  val name: CircuitName = circuitConfig.circuitName
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
    val bijection = UtilBijections.twitter2ScalaFuture[T](global)
    bijection
      .invert(breaker.withCircuitBreaker[T](bijection(body)))
      .rescue {
        case _: CircuitBreakerOpenException => Future.exception(CircuitBroken(name, "Open"))
        case _: FailedFastException => Future.exception(CircuitBroken(name, "Down"))
        case e: TimeoutException => Future.exception(CircuitTimeout(name))
        case e => Future.exception(e)
      }
  }

  private implicit def toScala(duration: Duration): FiniteDuration = FiniteDuration(duration.toMillis, MILLISECONDS)

  def stop() = system.terminate()
}
