package org.acme.kogito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import org.hamcrest.CoreMatchers;
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

        given().accept(ContentType.JSON)
                .when()
                .get("/persons")
                .then()
                .statusCode(200)
                .body("$.size()", is(0));
    }

    @Test
    public void testChild() {
        String processId = given()
                .body("{\"person\": {\"name\":\"Jenny Quark\", \"age\": 15}}")
                .contentType(ContentType.JSON)
                .when()
                .post("/persons")
                .then()
                .statusCode(201)
                .body("person.adult", is(false))
                .extract().path("id");

        String taskId = given().accept(ContentType.JSON)
                .when()
                .get("/persons/{uuid}/tasks?user=john", processId)
                .then()
                .statusCode(200)
                .body("$.size", is(1))
                .extract().path("[0].id");

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .body("{}")
                .when()
                .post("/persons/{uuid}/ChildrenHandling/{tuuid}?user=john", processId, taskId)
                .then().statusCode(200)
                .body("id", is(processId));

        given().accept(ContentType.JSON)
                .when()
                .get("/persons")
                .then()
                .statusCode(200)
                .body("$.size()", CoreMatchers.is(0));
    }
}
