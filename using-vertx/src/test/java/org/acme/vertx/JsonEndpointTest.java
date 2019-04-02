package org.acme.vertx;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

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
