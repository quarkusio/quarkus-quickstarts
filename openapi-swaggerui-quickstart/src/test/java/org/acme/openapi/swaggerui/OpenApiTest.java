package org.acme.openapi.swaggerui;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class OpenApiTest {

    @Test
    public void testOpenApi() {
        given()
                .when().get("/openapi")
                .then()
                .statusCode(200)
                .body(containsString("openapi"));
    }
}
