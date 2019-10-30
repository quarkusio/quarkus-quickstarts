package org.acme.vertx;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
class ResourceUsingWebClientTest {

    @Test
    void testLuke() {
        given()
                .when().get("/swapi/1")
                .then()
                .statusCode(200)
                .body(containsString("Luke Skywalker"));
    }

}