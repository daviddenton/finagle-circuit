package io.github.daviddenton.finaglecircuit.circuit

import com.twitter.util.Future
import io.github.daviddenton.finaglecircuit.{CircuitBreaker, CircuitName, CircuitStateChange}

class StubCircuitBreaker(val name: CircuitName) extends CircuitBreaker {

  private var callbacks: List[(CircuitStateChange) => Unit] = Nil

  def changeState(circuitStateChange: CircuitStateChange) = callbacks.foreach(_(circuitStateChange))

  override def withCircuit[V](body: => Future[V]) = body

  override def onCircuitStatusChange(state: (CircuitStateChange) => Unit) = {
    callbacks = state :: callbacks
    this
  }

  override def stop() = println("stopping")
}
