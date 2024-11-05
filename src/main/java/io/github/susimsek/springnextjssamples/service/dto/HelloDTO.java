package io.github.susimsek.springnextjssamples.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing a hello message response")
public record HelloDTO(

    @Schema(
        description = "The hello message from the backend",
        example = "Hello, Next.js!",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String message
) {}
