package io.github.susimsek.springnextjssamples.service

import io.github.susimsek.springnextjssamples.config.i18n.ParameterMessageSource
import io.github.susimsek.springnextjssamples.dto.response.HelloDTO
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
class HelloService(
  private val messageSource: ParameterMessageSource
) {

  private val messageKeys = listOf(
    "hello.message",
    "welcome.message",
    "greeting.message",
    "power.message",
    "random.message",
    "motivation.message"
  )

  fun getHelloMessageStream(locale: Locale): Flux<HelloDTO> {
    return Flux.interval(Duration.ofSeconds(5))
      .map {
        getRandomMessage(locale)
      }
  }

  fun getHelloMessage(locale: Locale): HelloDTO {
    val message = messageSource.getMessage("hello.message", null, locale)
    return HelloDTO(message, Instant.now())
  }

  private fun getRandomMessage(locale: Locale): HelloDTO {
    val randomKey = messageKeys.random()
    val message = messageSource.getMessage(randomKey, null, locale)
    return HelloDTO(message, Instant.now())
  }
}
