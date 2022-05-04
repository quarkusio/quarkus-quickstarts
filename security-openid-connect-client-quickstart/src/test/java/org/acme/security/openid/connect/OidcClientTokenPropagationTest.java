package org.acme.security.openid.connect;

import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.RestAssured;

@QuarkusTest
public class OidcClientTokenPropagationTest {

    KeycloakTestClient keycloakClient = new KeycloakTestClient();

    @Test
    public void testGetNameWithAdminTokenPropagated() {
    	String adminToken = getAccessToken("admin");
    	
        RestAssured.given().auth().oauth2(adminToken)
                .when().get("/frontend/user-name-with-propagated-token")
                .then()
                .statusCode(200)
                .body(is("admin"));

        RestAssured.given().auth().oauth2(adminToken)
                .when().get("/frontend/admin-name-with-propagated-token")
                .then()
                .statusCode(200)
                .body(is("admin"));
    }

    @Test
    public void testGetNameWithOidcClient() {
    	
        RestAssured.given()
                .when().get("/frontend/user-name-with-oidc-client-token")
                .then()
                .statusCode(200)
                .body(is("alice"));

        RestAssured.given()
                .when().get("/frontend/admin-name-with-oidc-client-token")
                .then()
                .statusCode(403);
    }

    protected String getAccessToken(String userName) {
        return keycloakClient.getAccessToken(userName);
    }
}
