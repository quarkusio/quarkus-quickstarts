package org.acme.kogito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class PersonProcessTest {

    @Test
    public void testAdult() {
        given()
                .body("{\"person\": {\"name\":\"John Quark\", \"age\": 20}}")
                .contentType(ContentType.JSON)
                .when()
                .post("/persons")
                .then()
                .statusCode(201)
                .body("person.adult", is(true));
    }

    @Test
    public void testChild() {
        given()
                .body("{\"person\": {\"name\":\"Jenny Quark\", \"age\": 15}}")
                .contentType(ContentType.JSON)
                .when()
                .post("/persons")
                .then()
                .statusCode(201)
                .body("person.adult", is(false));
    }
}
