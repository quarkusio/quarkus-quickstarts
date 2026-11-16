package org.acme.spring.test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class GreetingControllerTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/greeting")
                .then()
                .statusCode(200)
                .body(is("Hello, Quarkus!"));
    }

    @Test
    public void testGreetingEndpoint() {
        given()
                .pathParam("name", "World")
                .when().get("/greeting/{name}")
                .then()
                .statusCode(200)
                .body(is("Hello, World!"));
    }
}