package org.acme.security.openid.connect;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(KeycloakServer.class)
public class BearerTokenAuthenticationTest {

    @Test
    public void testAdminAccess() {
        RestAssured.given().auth().oauth2(getAccessToken("admin"))
                .when().get("/api/admin")
                .then()
                .statusCode(200);

        RestAssured.given().auth().oauth2(getAccessToken("alice"))
                .when().get("/api/admin")
                .then()
                .statusCode(403);
    }

    @Test
    public void testUserAccess() {

        RestAssured.given().auth().oauth2(getAccessToken("alice"))
                .when().get("/api/users/me")
                .then()
                .statusCode(200);

        RestAssured.given().auth().oauth2(getAccessToken("admin"))
                .when().get("/api/users/me")
                .then()
                .statusCode(200);
    }

    private String getAccessToken(String userName) {
        return RestAssured
                .given()
                .param("grant_type", "password")
                .param("username", userName)
                .param("password", userName)
                .param("client_id", "backend-service")
                .param("client_secret", "secret")
                .when()
                .post("http://localhost:8180/auth/realms/quarkus/protocol/openid-connect/token")
                .jsonPath().get("access_token");
    }
}
