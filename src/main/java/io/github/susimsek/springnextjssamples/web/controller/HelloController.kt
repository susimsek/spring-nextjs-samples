package io.github.susimsek.springnextjssamples.web.controller

import io.github.susimsek.springnextjssamples.config.i18n.ParameterMessageSource
import io.github.susimsek.springnextjssamples.dto.response.HelloDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@Tag(name = "hello", description = "Hello API")
@SecurityRequirement(name = "bearerAuth")
class HelloController(
    private val messageSource: ParameterMessageSource
) {

    @Operation(summary = "Say Hello", description = "Returns a simple Hello Next.js message in JSON format")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successful response",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = HelloDTO::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(example = "{\"error\": \"Internal Server Error\"}")
                )]
            )
        ]
    )
    @GetMapping("/api/v1/hello")
    fun hello(locale: Locale): HelloDTO {
        val message = messageSource.getMessage("hello.message", null, locale)
        return HelloDTO(message)
    }
}