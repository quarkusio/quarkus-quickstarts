package org.acme.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GreetingTest {

    @Test
    public void greetingTest() {
        String message = "hello";
        Greeting greeting = new Greeting(message);
        assertEquals(greeting.getMessage(), message);
    }
}
