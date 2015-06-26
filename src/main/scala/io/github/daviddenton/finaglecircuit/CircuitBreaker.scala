package io.github.daviddenton.finaglecircuit

import com.twitter.util.Future

trait CircuitBreaker {
  val name: CircuitName

  def withCircuit[T](body: => Future[T]): Future[T]

  def onCircuitStatusChange(state: CircuitStateChange => Unit): CircuitBreaker
}



