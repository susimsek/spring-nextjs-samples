package io.github.susimsek.springnextjssamples.web.controller

import io.github.susimsek.springnextjssamples.dto.response.TodoDTO
import io.github.susimsek.springnextjssamples.service.TodoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/todos")
@Tag(name = "todos", description = "Todo API")
@SecurityRequirement(name = "bearerAuth")
@Validated
class TodoController(
  private val todoService: TodoService
) {

  @Operation(summary = "Get All Todos", description = "Returns a list of all todos")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "Successful response",
        content = [Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          array = ArraySchema(schema = Schema(implementation = TodoDTO::class))
        )]
      )
    ]
  )
  @GetMapping
  fun getTodos(): List<TodoDTO> {
    return todoService.getAllTodos()
  }

  @Operation(summary = "Get Todo by ID", description = "Returns a specific todo by its ID")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "Successful response",
        content = [Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = Schema(implementation = TodoDTO::class)
        )]
      ),
      ApiResponse(
        responseCode = "400", description = "Invalid input",
        content = [Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = Schema(implementation = ProblemDetail::class)
        )]
      ),
      ApiResponse(
        responseCode = "404",
        description = "Todo not found",
        content = [Content(
          mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = Schema(implementation = ProblemDetail::class)
        )]
      )
    ]
  )
  @GetMapping("/{id}")
  fun getTodoById(
    @Parameter(description = "ID of the todo to be retrieved")
    @PathVariable @Min(value = 1, message = "{validation.field.min}")
    @NotNull(message = "{validation.field.notNull}") id: Int
  ): TodoDTO {
    return todoService.getTodoById(id)
  }
}
