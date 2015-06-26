package io.github.daviddenton.finaglecircuit.circuit

import com.twitter.finagle.Service
import com.twitter.finagle.http.Request
import com.twitter.io.Charsets
import com.twitter.util.{Await, Future}
import io.github.daviddenton.finaglecircuit.{CircuitBreaking, CircuitBroken, CircuitName}
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import org.scalatest.{FunSpec, ShouldMatchers}

class CircuitBreakingTest extends FunSpec with ShouldMatchers {

  private val name = CircuitName("mysystem")

  describe("CircuitBreaking") {
    val breaker = new StubCircuitBreaker(name)

    it("converts circuit broken messages into 503") {
      val a = Await.result(new CircuitBreaking(breaker).apply(Request("/"), Service.mk(r => Future.exception(CircuitBroken(name, "reason")))))
      a.getStatus shouldEqual SERVICE_UNAVAILABLE
      a.getContent.toString(Charsets.Utf8) shouldEqual "Call to mysystem failed: reason"
    }

    it("converts unexpected errors into 503") {
      val a = Await.result(new CircuitBreaking(breaker).apply(Request("/"), Service.mk(r => Future.exception(new RuntimeException("poo")))))
      a.getStatus shouldEqual SERVICE_UNAVAILABLE
      a.getContent.toString(Charsets.Utf8) shouldEqual "Call to mysystem failed: poo"
    }
  }
}
