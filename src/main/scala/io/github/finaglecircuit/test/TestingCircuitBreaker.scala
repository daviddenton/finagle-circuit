package io.github.finaglecircuit.test

import com.twitter.util.Future
import io.github.finaglecircuit.{CircuitBreaker, CircuitName, CircuitStateChange}

/**
 * Circuit breaker which can be used for testing purposes by forcing state changes.
 */
class TestingCircuitBreaker(val name: CircuitName) extends CircuitBreaker {

  private var callbacks: List[(CircuitStateChange) => Unit] = Nil

  /**
   * Force a state change on this breaker.
   */
  def changeState(circuitStateChange: CircuitStateChange) = callbacks.foreach(_(circuitStateChange))

  override def withCircuit[V](body: => Future[V]) = body

  override def onCircuitStatusChange(state: (CircuitStateChange) => Unit) = {
    callbacks = state :: callbacks
    this
  }

  override def stop() = println("stopping")
}
