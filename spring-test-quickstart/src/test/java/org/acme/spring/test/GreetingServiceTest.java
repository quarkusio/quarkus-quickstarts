package org.acme.spring.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GreetingServiceTest {

    @Autowired
    GreetingService greetingService;

    @Test
    public void testGreeting() {
        assertEquals("Hello, Quarkus!", greetingService.greet("Quarkus"));
    }
}