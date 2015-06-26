package io.github.daviddenton.finaglecircuit

/**
 * The exception thrown from the circuit breaker when the circuit is open.
 * @param circuitName the name
 * @param reason detail about why this exception was raised
 */
case class CircuitBroken(circuitName: CircuitName, reason: String) extends Exception(s"$circuitName circuit marked as $reason")
