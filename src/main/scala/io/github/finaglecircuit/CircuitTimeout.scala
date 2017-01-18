package io.github.finaglecircuit

/**
 * The exception thrown from the circuit breaker when the circuit times out.
 * @param circuitName the name
 */
case class CircuitTimeout(circuitName: CircuitName) extends Exception(s"$circuitName circuit timed out")
