package io.github.daviddenton.finaglecircuit

import java.time.Duration

case class CircuitConfig(circuitName: CircuitName, timeout: Duration, downTime: Duration, failLimit: Int) {
  override def toString: String = s"$circuitName/$timeout/$downTime/$failLimit"
}

object CircuitConfig {
  def apply(str: String): CircuitConfig = {
    val parts = str.split("/")
    CircuitConfig(CircuitName(parts(0)), Duration.parse(parts(1)), Duration.parse(parts(2)), parts(3).toInt)
  }
}
