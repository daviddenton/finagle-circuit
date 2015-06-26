package io.github.daviddenton.finaglecircuit

case class CircuitBroken(circuitName: CircuitName, reason: String) extends Exception(s"$circuitName circuit marked as $reason")
