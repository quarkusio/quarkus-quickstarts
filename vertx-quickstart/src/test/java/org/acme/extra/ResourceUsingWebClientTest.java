package org.acme.extra;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class ResourceUsingWebClientTest {

    @Test
    void testBananaData() {
        given()
                .when().get("/fruit-data/banana")
                .then()
                .statusCode(200)
                .body(containsString("Musaceae"));
    }

}
