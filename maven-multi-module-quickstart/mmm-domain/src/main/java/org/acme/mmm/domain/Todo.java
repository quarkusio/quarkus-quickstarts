package org.acme.mmm.domain;

public class Todo {

    private final long id;
    private final String text;

    public Todo(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
