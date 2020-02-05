package org.acme.vertx;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.startsWith;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class GreetingResourceTest {

    @Test
    void testGreeter() {
        given()
                .when().get("/hello/Quarkus")
                .then()
                .statusCode(200)
                .body(startsWith("Hello Quarkus!"));
    }
}
