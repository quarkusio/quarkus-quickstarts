package org.acme.elytron.security.jpa.reactive;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JpaSecurityReactiveRealmTest {

    @Test
    @Order(1)
    void createUser() {
        given()
                .auth().preemptive().basic("admin", "admin")
                .body("user")
                .post("/api/users")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @Order(2)
    void shouldAccessPublicWhenAnonymous() {
        get("/api/public")
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    @Order(3)
    void shouldNotAccessAdminWhenAnonymous() {
        get("/api/admin")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

    }

    @Test
    @Order(4)
    void shouldAccessAdminWhenAdminAuthenticated() {
        given()
                .auth().preemptive().basic("admin", "admin")
                .when()
                .get("/api/admin")
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    @Order(5)
    void shouldNotAccessUserWhenAdminAuthenticated() {
        given()
                .auth().preemptive().basic("admin", "admin")
                .when()
                .get("/api/users/me")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @Order(6)
    void shouldAccessUserAndGetIdentityWhenUserAuthenticated() {
        given()
                .auth().preemptive().basic("user", "user")
                .when()
                .get("/api/users/me")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("user"));
    }
}
