package org.acme.services;

import org.acme.domain.Greeting;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    public Greeting greeting() {
        return new Greeting("hello");
    }

    public Greeting greeting(String name) {
        String message = String.format("hello %s", name);
        return new Greeting(message);
    }
}
