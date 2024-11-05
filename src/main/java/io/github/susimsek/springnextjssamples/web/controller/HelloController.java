package io.github.susimsek.springnextjssamples.web.controller;

import io.github.susimsek.springnextjssamples.service.dto.HelloDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "hello", description = "Hello API")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class HelloController {

    private final MessageSource messageSource;

    @Operation(summary = "Say Hello", description = "Returns a simple Hello Next.js message in JSON format")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = HelloDTO.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(example = "{\"error\": \"Internal Server Error\"}"))
        )
    })
    @GetMapping("/api/v1/hello")
    public HelloDTO hello(Locale locale) {
        var message =  messageSource.getMessage("hello.message", null, locale);
        return new HelloDTO(message);
    }
}
