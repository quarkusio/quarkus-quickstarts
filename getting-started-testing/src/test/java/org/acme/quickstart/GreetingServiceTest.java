package org.acme.quickstart;

import javax.inject.Inject;

import io.quarkus.test.junit.DisabledOnNativeImage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GreetingServiceTest {

    @Inject
    GreetingService service;

    @Test
    @DisabledOnNativeImage("@Inject in tests doesn't work for native mode")
    public void testGreetingService() {
        Assertions.assertEquals("hello Quarkus", service.greeting("Quarkus"));
    }
}
