package org.acme.extra;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class JsonEndpointTest {

    @Test
    void testJsonObject() {
        given()
                .when().get("/hello/Quarkus/object")
                .then()
                .statusCode(200)
                .body("Hello", is(equalTo("Quarkus")));
    }

    @Test
    void testJsonArray() {
        given()
                .when().get("/hello/Quarkus/array")
                .then()
                .statusCode(200)
                .body("", is(equalTo(Arrays.asList("Hello", "Quarkus"))));
    }

}
