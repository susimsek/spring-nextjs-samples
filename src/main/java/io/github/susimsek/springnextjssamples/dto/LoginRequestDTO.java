package io.github.susimsek.springnextjssamples.dto;

import io.github.susimsek.springnextjssamples.validation.AlphaNumeric;
import io.github.susimsek.springnextjssamples.validation.ConstraintMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Login request dto containing username and password")
public record LoginRequestDTO(
    @Schema(
        description = "The username of the user",
        example = "admin",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = ConstraintMessage.NOT_BLANK)
    @Size(min = 3, max = 50, message = ConstraintMessage.SIZE)
    @AlphaNumeric
    String username,

    @Schema(
        description = "The password of the user",
        example = "password",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = ConstraintMessage.NOT_BLANK)
    @Size(min = 4, max = 100, message = ConstraintMessage.SIZE)
    String password
) {}
