package io.github.susimsek.springnextjssamples.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "DTO representing a hello message response")
public record HelloDTO(

    @Schema(
        description = "The hello message from the backend",
        example = "Hello, Next.js!",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String message,

    @Schema(
        description = "The timestamp of when the message was created",
        example = "2023-11-10T10:15:30Z",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    Instant timestamp
) {}
