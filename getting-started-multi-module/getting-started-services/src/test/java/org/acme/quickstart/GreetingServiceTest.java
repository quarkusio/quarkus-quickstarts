package org.acme.quickstart;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GreetingServiceTest {

    GreetingService service = new GreetingService();

    @Test
    public void testGreeting() {
        assertEquals("hello world", service.greeting("world"));
    }
}
