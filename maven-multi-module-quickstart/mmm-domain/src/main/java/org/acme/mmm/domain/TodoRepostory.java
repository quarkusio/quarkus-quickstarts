package org.acme.mmm.domain;

import java.util.Optional;

public interface TodoRepostory {

    Todo saveTodo(String todoText);

    Optional<Todo> todoById(long id);
}
