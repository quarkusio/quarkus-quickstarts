package org.acme.elytron.security.ldap;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;

@QuarkusTest
class ElytronSecurityLdapTest {

    @Test
    void anonymous() {
        RestAssured.given()
                .when()
                .get("/api/public")
                .then()
                .statusCode(200)
                .body(containsString("public"));
    }

    @Test
    void adminResourceAnonymous() {
        RestAssured.given()
                .redirects().follow(false)
                .when()
                .get("/api/admin")
                .then()
                .statusCode(401);
    }

    @Test
    void adminResourceAuthorized() {
        RestAssured.given()
                .when()
                .auth().preemptive().basic("adminUser", "adminUserPassword")
                .get("/api/admin")
                .then()
                .statusCode(200);
    }

    @Test
    void userResourceForbidden() {
        RestAssured.given()
                .redirects().follow(false)
                .when()
                .auth().preemptive().basic("adminUser", "adminUserPassword")
                .get("/api/users/me")
                .then()
                .statusCode(403);
    }

    @Test
    void userResourceAuthorized() {
        RestAssured.given()
                .redirects().follow(false)
                .when()
                .auth().preemptive().basic("standardUser", "standardUserPassword")
                .get("/api/users/me")
                .then()
                .statusCode(200);
    }

}
