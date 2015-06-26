package io.github.daviddenton.finaglecircuit

object CircuitStatus extends Enumeration {
  type CircuitStatus = Value
  val Open, Closed, HalfOpen = Value
}
