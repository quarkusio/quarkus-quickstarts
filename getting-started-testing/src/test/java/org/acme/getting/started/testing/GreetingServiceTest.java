package org.acme.getting.started.testing;

import jakarta.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GreetingServiceTest {

    @Inject
    GreetingService service;

    @Test
    public void testGreetingService() {
        assertThat(service.greeting("Quarkus")).isEqualTo("hello Quarkus");
    }
}
