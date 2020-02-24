package org.acme.security.jdbc;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import io.quarkus.test.junit.QuarkusTest;

@Testcontainers
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JdbcSecurityRealmTest {

    @Container
    public static final PostgreSQLContainer DATABASE = new PostgreSQLContainer<>()
            .withDatabaseName("elytron_security_jdbc")
            .withUsername("quarkus")
            .withPassword("quarkus")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd -> cmd
                    .withHostName("localhost")
                    .withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432))))
            .withInitScript("test_user.sql");

    @Test
    @Order(1)
    void shouldAccessPublicWhenAnonymous() {
        get("/api/public")
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    @Order(2)
    void shouldNotAccessAdminWhenAnonymous() {
        get("/api/admin")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

    }

    @Test
    @Order(3)
    void shouldAccessAdminWhenAdminAuthenticated() {
        given()
                .auth().preemptive().basic("admin", "admin")
                .when()
                .get("/api/admin")
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    @Order(4)
    void shouldNotAccessUserWhenAdminAuthenticated() {
        given()
                .auth().preemptive().basic("admin", "admin")
                .when()
                .get("/api/users/me")
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @Order(5)
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
