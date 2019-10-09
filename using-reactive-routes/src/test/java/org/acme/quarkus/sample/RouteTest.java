package org.acme.quarkus.sample;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.Is.is;

@QuarkusTest
public class RouteTest {

    @Test
    public void testDeclarativeRoutes() {
        RestAssured.get("/").then()
                .header("X-Header", "intercepting the request")
                .statusCode(200)
                .body(is("hello"));

        RestAssured.get("/hello").then()
                .header("X-Header", "intercepting the request")
                .statusCode(200)
                .body(is("hello world"));

        RestAssured.get("/hello?name=quarkus").then()
                .header("X-Header", "intercepting the request")
                .statusCode(200)
                .body(is("hello quarkus"));
    }

    @Test
    public void testMyRoute() {
        RestAssured.get("/my-route").then()
                .header("X-Header", "intercepting the request")
                .statusCode(200)
                .body(is("Hello from my route"));
    }


}
