package io.github.susimsek.springnextjssamples.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT token response dto")
public record TokenDTO(
    @Schema(
        description = "JWT access token",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String accessToken,

    @Schema(
        description = "Token type",
        example = "Bearer",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String tokenType,

    @Schema(
        description = "Access token expiration time in seconds",
        example = "3600",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    long accessTokenExpiresIn
) {}
