package io.github.finaglecircuit

import java.time.Duration

/**
 * Configuration of a circuit.
 * @param circuitName the name of the circuit (or downstream system) to use.
 * @param timeout the duration after which a call will be counted as a failure.
 * @param downTime when the circuit is broken, it will not attempt to pass through any traffic for at least
 *                 this amount of time.
 * @param failLimit the number of consecutive failures before tripping the circuit.
 */
case class CircuitConfig(circuitName: CircuitName, timeout: Duration, downTime: Duration, failLimit: Int) {
  override def toString: String = s"$circuitName/$timeout/$downTime/$failLimit"
}

object CircuitConfig {
  def apply(str: String): CircuitConfig = {
    val parts = str.split("/")
    CircuitConfig(CircuitName(parts(0)), Duration.parse(parts(1)), Duration.parse(parts(2)), parts(3).toInt)
  }
}
