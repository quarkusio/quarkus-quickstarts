package org.acme.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GreetingServiceTest {

    public GreetingService greetingService = new GreetingService();

    @Test
    public void greetingServiceTest() {
        String message = "hello";
        String response = greetingService.greeting().getMessage();
        assertEquals(response, message);
    }

    @Test void greetingServiceWithNameTest() {
        String name = "john";
        String message = "hello john";
        String response = greetingService.greeting(name).getMessage();
        assertEquals(response, message);
    }
}
