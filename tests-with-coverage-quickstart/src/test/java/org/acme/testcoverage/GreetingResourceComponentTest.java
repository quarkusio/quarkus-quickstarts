package org.acme.testcoverage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.quarkus.test.component.QuarkusComponentTest;
import io.quarkus.test.component.TestConfigProperty;
import jakarta.inject.Inject;

@QuarkusComponentTest
public class GreetingResourceComponentTest {

    @Inject
    GreetingResource greetingResource;

    @Test
    @TestConfigProperty(key = "hi.message", value = "Hola")
    public void testResource() {
        assertEquals("Hola Hugo!", greetingResource.hi("Hugo"));
    }

}
