package io.github.daviddenton.finaglecircuit

import com.twitter.finagle.http.Response
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Future
import org.jboss.netty.handler.codec.http.HttpResponseStatus._
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}

class CircuitBreaking(circuitBreaker: CircuitBreaker) extends SimpleFilter[HttpRequest, HttpResponse] {

  override def apply(request: HttpRequest, next: Service[HttpRequest, HttpResponse]): Future[HttpResponse] = {
    circuitBreaker.withCircuit[HttpResponse](next.apply(request)).handle {
      case e: CircuitBroken => {
        val response = Response(SERVICE_UNAVAILABLE)
        response.setContentString(s"Call to ${circuitBreaker.name} failed: ${e.reason}")
        response
      }
      case e: Exception => {
        val response = Response(SERVICE_UNAVAILABLE)
        response.setContentString(s"Call to ${circuitBreaker.name} failed: ${e.getMessage}")
        response
      }
    }
  }
}
