package examples

import java.time.Duration

import com.twitter.finagle.Httpx
import com.twitter.finagle.httpx.Request
import com.twitter.util.Await.result
import io.github.finaglecircuit.{AkkaCircuitBreaker, CircuitBreaking, CircuitConfig, CircuitName}

/**
 * Example of using a Circuit. After the first 2 failures, the circuit is tripped for the specified timeout.
 */
object CircuitBreakingService extends App {

  val callFailLimit = 2
  val circuitConfig = CircuitConfig(CircuitName("aDownstreamSystem"), Duration.ofSeconds(1), Duration.ofSeconds(1), callFailLimit)

  val breaker = new AkkaCircuitBreaker(circuitConfig)
    .onCircuitStatusChange(println)

  val protectedService = new CircuitBreaking(breaker)
    .andThen(Httpx.newService("not-a-service.com:19999"))

  println(result(protectedService(Request("/"))).status)
  println(result(protectedService(Request("/"))).status)
  println(result(protectedService(Request("/"))).status)

  breaker.stop()
}
