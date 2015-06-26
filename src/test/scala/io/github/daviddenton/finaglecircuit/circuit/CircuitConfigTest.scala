package io.github.daviddenton.finaglecircuit.circuit

import java.time.Duration

import io.github.daviddenton.finaglecircuit.{CircuitConfig, CircuitName}
import org.scalatest.{FunSpec, ShouldMatchers}

class CircuitConfigTest extends FunSpec with ShouldMatchers {
  describe("CircuitConfig") {
    it("round trips") {
      val config = CircuitConfig(CircuitName("mycircuit"), Duration.ofMillis(500), Duration.ofMillis(600), 2)
      CircuitConfig(config.toString) shouldEqual config
    }
  }
}
