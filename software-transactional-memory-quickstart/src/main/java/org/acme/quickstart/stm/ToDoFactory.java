package org.acme.quickstart.stm;

import org.jboss.stm.Container;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class ToDoFactory {
    private ToDo toDoHandle;

    private void initToDoFactory () {
        Container<ToDo> container = new Container<>();
        toDoHandle = container.create(new ToDoImpl());
    }

    ToDo getInstance() {
        if (toDoHandle == null) {
            initToDoFactory();
        }
        return toDoHandle;
    }
}
