package org.acme.mmm.infrastructure;

import org.acme.mmm.domain.Todo;
import org.acme.mmm.domain.TodoRepostory;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class InMemoryTodoRepository implements TodoRepostory {

    private long nextId = 0;
    Map<Long, Todo> todos;

    public InMemoryTodoRepository() {
        this.todos = new HashMap<>();
    }

    @Override
    public Todo saveTodo(String todoText) {
        Todo newTodo = new Todo(nextId++, todoText);
        todos.put(newTodo.getId(), newTodo);
        return newTodo;
    }

    @Override
    public Optional<Todo> todoById(long id) {
        return Optional.ofNullable(todos.get(id));
    }
}
