package io.github.finaglecircuit

import java.time.Duration

import org.scalatest.{FunSpec, Matchers}

class CircuitConfigTest extends FunSpec with Matchers {
  describe("CircuitConfig") {
    it("round trips") {
      val config = CircuitConfig(CircuitName("mycircuit"), Duration.ofMillis(500), Duration.ofMillis(600), 2)
      CircuitConfig(config.toString) shouldEqual config
    }
  }
}
