package io.github.susimsek.springnextjssamples.web.controller.graphql

import io.github.susimsek.springnextjssamples.dto.response.TodoDTO
import io.github.susimsek.springnextjssamples.service.TodoService
import jakarta.validation.constraints.Min
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import java.util.logging.Logger

@GraphQLController
@PreAuthorize("hasRole('ADMIN')")
class TodoGraphQLController(
  private val todoService: TodoService
) {

  private val logger = Logger.getLogger(TodoGraphQLController::class.java.name)

  @QueryMapping
  fun todos(authentication: Authentication): List<TodoDTO> {
    logAccess(authentication, "todos")
    return todoService.getAllTodos()
  }

  @QueryMapping
  fun todo(
    authentication: Authentication,
    @Argument @Min(value = 1, message = "{validation.field.min}") id: Int
  ): TodoDTO {
    logAccess(authentication, "todo")
    return todoService.getTodoById(id)
  }

  private fun logAccess(authentication: Authentication, methodName: String) {
    val username = authentication.name
    logger.info("Access Log - User: $username, Method: $methodName")
  }
}
