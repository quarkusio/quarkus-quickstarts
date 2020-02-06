package org.acme.testcoverage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GreetingServiceTest {

    @Test
    public void testGreetingService() {
        GreetingService service = new GreetingService();
        Assertions.assertEquals("hello Quarkus", service.greeting("Quarkus"));
    }
}