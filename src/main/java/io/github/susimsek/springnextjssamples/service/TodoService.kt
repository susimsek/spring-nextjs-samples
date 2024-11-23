package io.github.susimsek.springnextjssamples.service

import io.github.susimsek.springnextjssamples.client.TodoClient
import io.github.susimsek.springnextjssamples.dto.response.TodoDTO
import io.github.susimsek.springnextjssamples.exception.ResourceNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException

@Service
class TodoService(private val todoClient: TodoClient) {

  /**
   * Get all todos.
   *
   * @return a list of TodoDTO objects
   */
  fun getAllTodos(): List<TodoDTO> {
    return todoClient.getTodos()
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
