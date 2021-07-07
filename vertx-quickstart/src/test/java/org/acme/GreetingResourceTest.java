package org.acme;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.startsWith;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class GreetingResourceTest {

    @Test
    void testGreeter() {

        given()
                .queryParam("name", "Quarkus")
                .when().get("/vertx/hello")
                .then()
                .statusCode(200)
                .body(startsWith("Hello Quarkus"));
    }
}
