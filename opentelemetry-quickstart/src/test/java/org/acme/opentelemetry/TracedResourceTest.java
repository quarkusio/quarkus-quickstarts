package org.acme.opentelemetry;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TracedResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }

    @Test
    public void testChainEndpoint() {
        given()
                .when().get("/chain")
                .then()
                .statusCode(200)
                .body(is("chain -> hello"));
    }

    @Test
    public void testSetMetricEndpoint() {
        long testValue = 42L;

        given()
                .queryParam("value", testValue)
                .when().get("/metrics/set")
                .then()
                .statusCode(200)
                .body("status", is("success"))
                .body("xvalue", is((int)testValue));
    }

}
