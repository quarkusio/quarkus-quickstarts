package org.acme.vertx;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.startsWith;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class EventResourceTest {

    @Test
    void testEventBusGreeter() {
        given()
                .when().get("/async/Quarkus")
                .then()
                .statusCode(200)
                .body(startsWith("Hello Quarkus"));
    }
}
