package org.acme.openapi.swaggerui;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class OpenApiTest {

    @Test
    public void testOpenApi() {
        given()
                .when().get("/q/openapi")
                .then()
                .statusCode(200)
                .body(containsString("openapi"));
    }
}
