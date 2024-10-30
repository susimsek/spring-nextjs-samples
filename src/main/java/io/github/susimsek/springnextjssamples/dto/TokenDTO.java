package io.github.susimsek.springnextjssamples.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT token response dto")
public record TokenDTO(
    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String accessToken,

    @Schema(description = "Token type", example = "Bearer")
    String tokenType,

    @Schema(description = "Access token expiration time in seconds", example = "3600")
    long accessTokenExpiresIn
) {}
