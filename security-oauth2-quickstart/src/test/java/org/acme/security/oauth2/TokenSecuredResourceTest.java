package org.acme.security.oauth2;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;

@QuarkusTest
@QuarkusTestResource(MockAuthorizationServerTestResource.class)
class TokenSecuredResourceTest {
    // use whatever token you want as the mock OAuth server will accept all tokens
    private static final String BEARER_TOKEN = "337aab0f-b547-489b-9dbd-a54dc7bdf20d";

    @Test
    void testPermitAll() {
        RestAssured.given()
                .when()
                .header("Authorization", "Bearer: " + BEARER_TOKEN)
                .get("/secured/permit-all")
                .then()
                .statusCode(200)
                .body(containsString("hello"));
    }

    @Test
    void testRolesAllowed() {
        RestAssured.given()
                .when()
                .header("Authorization", "Bearer: " + BEARER_TOKEN)
                .get("/secured/roles-allowed")
                .then()
                .statusCode(200)
                .body(containsString("hello"));
    }
}