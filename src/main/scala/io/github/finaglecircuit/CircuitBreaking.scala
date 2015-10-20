package io.github.finaglecircuit

import com.twitter.finagle.httpx.Status._
import com.twitter.finagle.httpx.{Request, Response}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util.Future

/**
 * An HTTP filter which adds circuit breaking to any service. CAn be used on service or client side.
 * Converts unexpected exceptions and broken circuit problems into 503 errors to the upstream client.
 * @param circuitBreaker the breaker to use
 */
class CircuitBreaking(circuitBreaker: CircuitBreaker) extends SimpleFilter[Request, Response] {

  override def apply(request: Request, next: Service[Request, Response]): Future[Response] = {
    circuitBreaker.withCircuit[Response](next.apply(request)).handle {
      case e: CircuitBroken => {
        val response = Response(ServiceUnavailable)
        response.setContentString(s"Call to ${circuitBreaker.name} failed: ${e.reason}")
        response
      }
      case e: Exception => {
        val response = Response(ServiceUnavailable)
        response.setContentString(s"Call to ${circuitBreaker.name} failed: ${e.getMessage}")
        response
      }
    }
  }
}
