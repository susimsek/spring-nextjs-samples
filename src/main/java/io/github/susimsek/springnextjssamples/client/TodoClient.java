package io.github.susimsek.springnextjssamples.client;

import io.github.susimsek.springnextjssamples.dto.response.TodoDTO;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "/todos")
public interface TodoClient {

    @GetExchange
    List<TodoDTO> getTodos(
        @RequestParam(name = "_page", required = false) Integer page,
        @RequestParam(name = "_limit", required = false) Integer limit
    );

    @GetExchange("/{id}")
    TodoDTO getTodoById(@PathVariable Integer id);
}
