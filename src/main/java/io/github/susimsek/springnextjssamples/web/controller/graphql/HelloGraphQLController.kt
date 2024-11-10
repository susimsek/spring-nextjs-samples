package io.github.susimsek.springnextjssamples.web.controller.graphql

import io.github.susimsek.springnextjssamples.dto.response.HelloDTO
import io.github.susimsek.springnextjssamples.service.HelloService
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import java.util.*
import java.util.logging.Logger

@Controller
class HelloGraphQLController(
  private val helloService: HelloService
) {

  private val logger = Logger.getLogger(HelloGraphQLController::class.java.name)

  @QueryMapping
  @PreAuthorize("hasRole('ADMIN')")
  fun hello(authentication: Authentication, locale: Locale): HelloDTO {
    logAccess(authentication, "hello")

    return helloService.getHelloMessage(locale)
  }

  @SubscriptionMapping
  @PreAuthorize("hasRole('ADMIN')")
  fun helloSubscription(authentication: Authentication, locale: Locale): Flux<HelloDTO> {
    logAccess(authentication, "helloSubscription")

    return helloService.getHelloMessageStream(locale)
  }

  private fun logAccess(authentication: Authentication, methodName: String) {
    val username = authentication.name
    val timestamp = LocalDateTime.now()

    logger.info("Access Log - User: $username, Method: $methodName, Time: $timestamp")
  }
}
