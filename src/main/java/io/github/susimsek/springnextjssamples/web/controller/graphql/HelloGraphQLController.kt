package io.github.susimsek.springnextjssamples.web.controller.graphql

import io.github.susimsek.springnextjssamples.dto.response.HelloDTO
import io.github.susimsek.springnextjssamples.service.HelloService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SubscriptionMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import reactor.core.publisher.Flux
import java.time.LocalDateTime
import java.util.*
import java.util.logging.Logger

@GraphQLController
@PreAuthorize("hasRole('ADMIN')")
class HelloGraphQLController(
  private val helloService: HelloService
) {

  private val logger = Logger.getLogger(HelloGraphQLController::class.java.name)

  @QueryMapping
  fun hello(authentication: Authentication, locale: Locale): HelloDTO {
    logAccess(authentication, "hello")

    return helloService.getHelloMessage(locale)
  }

  @SubscriptionMapping
  fun helloSubscription(
    authentication: Authentication,
    @Argument locale: Locale
  ): Flux<HelloDTO> {
    logAccess(authentication, "helloSubscription")

    return helloService.getHelloMessageStream(locale)
  }

  private fun logAccess(authentication: Authentication, methodName: String) {
    val username = authentication.name
    val timestamp = LocalDateTime.now()

    logger.info("Access Log - User: $username, Method: $methodName, Time: $timestamp")
  }
}
