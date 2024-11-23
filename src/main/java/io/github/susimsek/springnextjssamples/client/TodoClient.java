package io.github.susimsek.springnextjssamples.client;

import io.github.susimsek.springnextjssamples.dto.response.TodoDTO;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "/todos")
public interface TodoClient {

    @GetExchange
    List<TodoDTO> getTodos();

    @GetExchange("/{id}")
    TodoDTO getTodoById(@PathVariable Integer id);
}
