package org.acme.microprofile.health;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class HealthCheckTest {

    @Test
    public void testHealthCheck() {
        given()
                .when()
                .get("/q/health/live")
                .then()
                .statusCode(200)
                .body("status", is("UP"))
                .body("checks.size()", is(3))
                .body("checks.name", everyItem(anyOf(
                        is("Simple health check"),
                        is("Health check with data"),
                        is("liveness-reactive"))))
                .body("checks.status", everyItem(is("UP")))
                .body("checks.data.foo[0]", is("fooValue"))
                .body("checks.data.bar[0]", is("barValue"));
    }

    @Test
    public void testReadinessCheck() {
        given()
                .when()
                .get("/q/health/ready")
                .then()
                .statusCode(200)
                .body("status", is("UP"))
                .body("checks.size()", is(1))
                .body("checks.name[0]", is("Database connection health check"));
    }

    @Test
    public void testStartupCheck() {
        given()
                .when()
                .get("/q/health/started")
                .then()
                .statusCode(200)
                .body("status", is("UP"))
                .body("checks.size()", is(1))
                .body("checks.name[0]", is("Startup health check"));
    }

    @Test
    public void testWellnessCheck() {
        given()
                .when()
                .get("/q/health/well")
                .then()
                .statusCode(200)
                .body("status", is("UP"))
                .body("checks.size()", is(1))
                .body("checks.name[0]", is("Wellness health check"));
    }

    @Test
    public void testHealthGroup() {
        given()
                .when()
                .get("/q/health/group/external-services")
                .then()
                .statusCode(200)
                .body("status", is("UP"))
                .body("checks.size()", is(1))
                .body("checks.name[0]", is("External service health check"));

        given()
                .when()
                .get("/q/health")
                .then()
                .statusCode(200)
                .body("checks.name", not(hasItem("External service health check")));
    }
}
