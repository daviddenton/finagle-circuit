package io.github.finaglecircuit

import com.twitter.util.Future

trait CircuitBreaker {
  val name: CircuitName

  /**
   * Wrap the passed call in a circuit breaking context. Possibly fails with a CircuitBroken failure in the future result.
   */
  def withCircuit[T](body: => Future[T]): Future[T]

  /**
   * Event callback for when the circuit changes state
   * @param state the new state of the circuit
   */
  def onCircuitStatusChange(state: CircuitStateChange => Unit): CircuitBreaker

  /**
   * Shutdown the circuit breaker.
   */
  def stop(): Unit
}



