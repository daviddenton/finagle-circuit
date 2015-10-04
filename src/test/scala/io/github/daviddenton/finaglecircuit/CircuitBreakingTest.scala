package io.github.daviddenton.finaglecircuit

import com.twitter.finagle.Service
import com.twitter.finagle.httpx.Request
import com.twitter.finagle.httpx.Status._
import com.twitter.util.{Await, Future}
import io.github.daviddenton.finaglecircuit.test.TestingCircuitBreaker
import org.scalatest.{FunSpec, ShouldMatchers}

class CircuitBreakingTest extends FunSpec with ShouldMatchers {

  private val name = CircuitName("mysystem")

  describe("CircuitBreaking") {
    val breaker = new TestingCircuitBreaker(name)

    it("converts circuit broken messages into 503") {
      val a = Await.result(new CircuitBreaking(breaker).apply(Request("/"), Service.mk(r => Future.exception(CircuitBroken(name, "reason")))))
      a.status shouldEqual ServiceUnavailable
      a.contentString shouldEqual "Call to mysystem failed: reason"
    }

    it("converts unexpected errors into 503") {
      val a = Await.result(new CircuitBreaking(breaker).apply(Request("/"), Service.mk(r => Future.exception(new RuntimeException("poo")))))
      a.status shouldEqual ServiceUnavailable
      a.contentString shouldEqual "Call to mysystem failed: poo"
    }
  }
}
