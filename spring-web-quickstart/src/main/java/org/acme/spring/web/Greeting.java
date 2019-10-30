package org.acme.spring.web;

public class Greeting {

    private final String message;

    public Greeting(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
