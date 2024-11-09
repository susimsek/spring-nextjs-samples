package io.github.susimsek.springnextjssamples.web.controller.graphql

import io.github.susimsek.springnextjssamples.config.i18n.ParameterMessageSource
import io.github.susimsek.springnextjssamples.dto.response.HelloDTO
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.access.prepost.PreAuthorize
import java.time.Instant
import java.util.*

@GraphQLController
class HelloGraphQLController(
    private val messageSource: ParameterMessageSource
) {

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun hello(locale: Locale): HelloDTO {
      val message = messageSource.getMessage("hello.message", null, locale)
      val timestamp = Instant.now()
      return HelloDTO(message, timestamp)
    }
}
