package org.acme.getting.started;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testGreetingWithPathParam() {
        given()
            .pathParam("name", "John")
            .when()
            .get("/hello/{name}")
            .then()
            .statusCode(200)
            .contentType(ContentType.TEXT)
            .body(is("Hello John from Quarkus REST"));
    }
}