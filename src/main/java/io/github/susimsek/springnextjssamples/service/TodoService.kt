package io.github.susimsek.springnextjssamples.service

import io.github.susimsek.springnextjssamples.client.TodoClient
import io.github.susimsek.springnextjssamples.dto.response.TodoDTO
import io.github.susimsek.springnextjssamples.exception.ResourceNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException

@Service
class TodoService(private val todoClient: TodoClient) {

  /**
   * Get all todos with pagination support.
   *
   * @param pageable pagination information (page number and size)
   * @return a list of TodoDTO objects for the requested page
   */
  fun getAllTodos(pageable: Pageable): List<TodoDTO> {
    val page = pageable.pageNumber + 1 // JSON Placeholder uses 1-based indexing
    val limit = pageable.pageSize
    return todoClient.getTodos(page, limit)
  }

  /**
   * Get a todo by its ID.
   *
   * @param id the ID of the todo
   * @return the TodoDTO if found
   * @throws ResourceNotFoundException if the todo is not found
   */
  fun getTodoById(id: Int): TodoDTO {
    return try {
      todoClient.getTodoById(id)
    } catch (ex: HttpClientErrorException.NotFound) {
      throw ResourceNotFoundException("Todo", "id", id)
    }
  }
}
