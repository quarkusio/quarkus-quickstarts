package org.acme.service;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {
    public String hello(String greet) {
        return "Hello " + greet;
    }
}
