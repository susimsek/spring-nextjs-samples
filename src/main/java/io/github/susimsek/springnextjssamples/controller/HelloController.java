package io.github.susimsek.springnextjssamples.controller;

import io.github.susimsek.springnextjssamples.dto.HelloDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "hello", description = "Hello API")
public class HelloController {

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
    @GetMapping("/api/hello")
    public HelloDTO hello() {
        return new HelloDTO("Hello, Next.js!");
    }
}
