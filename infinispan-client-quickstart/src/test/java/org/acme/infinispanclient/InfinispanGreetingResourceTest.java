package org.acme.infinispanclient;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.is;

@Testcontainers
@QuarkusTest
class InfinispanGreetingResourceTest {

    @Container
    public static final GenericContainer INFINISPAN = new FixedHostPortGenericContainer(
            "jboss/infinispan-server:latest")
            .withFixedExposedPort(11222, 11222)
            .withEnv("USER", "Titus Bramble")
            .withEnv("PASS", "Shambles")
            .waitingFor(Wait.forLogMessage(".*Infinispan Server .* started in .*", 1));

    @Test
    public void test() {
        RestAssured
                .given()
                .get("/infinispan")
                .then()
                .statusCode(200)
                .body(is("Hello World, Infinispan is up!"));
    }

}