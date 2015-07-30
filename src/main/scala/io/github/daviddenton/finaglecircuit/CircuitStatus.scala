package io.github.daviddenton.finaglecircuit

/**
 * Circuit states. Of note is HalfOpen:
 * - The first call attempted is allowed through without failing fast
 * - All other calls fail-fast with an exception just as in Open state
 * - If the first call succeeds, the breaker is reset back to Closed state
 * - If the first call fails, the breaker is tripped again into the Open state for another full resetTimeout
 */
object CircuitStatus extends Enumeration {
  type CircuitStatus = Value
  val Open, Closed, HalfOpen = Value
}
