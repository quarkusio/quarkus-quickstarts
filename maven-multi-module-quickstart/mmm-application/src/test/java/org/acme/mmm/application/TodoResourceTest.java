package org.acme.mmm.application;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
class TodoResourceTest {

    @Test
    void createTodo() {
        String someTodo = "someTodo";

        given()
                .when().body(someTodo).post("/todo")
                .then()
                .statusCode(200)
                .body("id", equalTo(0))
                .body("text", equalTo(someTodo));
    }
}