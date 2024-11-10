package org.acme.spring.di;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/greeting")
                .then()
                .statusCode(200)
                .body(is("HELLO WORLD!"));
    }

}
