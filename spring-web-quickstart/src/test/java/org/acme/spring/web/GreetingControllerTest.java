package org.acme.spring.web;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingControllerTest {

    @Test
    public void testGreeting() {
        given()
            .when().get("/greeting/world")
            .then()
                .statusCode(200)
                .body("message", is("HELLO WORLD!"));
    }
}
