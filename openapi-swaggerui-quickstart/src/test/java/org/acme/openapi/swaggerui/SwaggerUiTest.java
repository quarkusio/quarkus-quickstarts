package org.acme.openapi.swaggerui;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class SwaggerUiTest {

    // Note: Swagger UI is only available when Quarkus is started in dev or test mode
    @Test
    public void testSwaggerUi() {
        given()
                .when().get("/swagger-ui")
                .then()
                .statusCode(200)
                .body(containsString("url: \"/openapi\""));
    }
}
