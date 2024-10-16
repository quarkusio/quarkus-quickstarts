package org.acme.extra;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class ResourceUsingWebClientTest {

    @Test
    void testStarWarsData() {
        given()
                .when().get("/character-data/1")
                .then()
                .statusCode(200)
                .body(containsString("Luke Skywalker"));

    }

}
