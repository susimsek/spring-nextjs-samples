package io.github.susimsek.springnextjssamples.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing a TODO item")
public record TodoDTO(

    @Schema(
        description = "The ID of the user who owns the TODO item",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    Integer userId,

    @Schema(
        description = "The unique ID of the TODO item",
        example = "1",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    Integer id,

    @Schema(
        description = "The title of the TODO item",
        example = "delectus aut autem",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    String title,

    @Schema(
        description = "The completion status of the TODO item",
        example = "false",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    Boolean completed
) {
}
