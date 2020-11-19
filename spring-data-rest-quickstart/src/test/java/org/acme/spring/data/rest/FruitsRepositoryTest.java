package org.acme.spring.data.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsNot.not;

@QuarkusTest
class FruitsRepositoryTest {

    @Test
    void testListAllFruits() {
        //List all, should have all 3 fruits the database has initially:
        given()
                .accept("application/json")
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        containsString("Cherry"),
                        containsString("Apple"),
                        containsString("Banana")
                );

        //Delete the Cherry:
        given()
                .when().delete("/fruits/1")
                .then()
                .statusCode(204);

        //List all, cherry should be missing now:
        given()
                .accept("application/json")
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        not(containsString("Cherry")),
                        containsString("Apple"),
                        containsString("Banana")
                );

        //Create a new Fruit
        given()
                .contentType("application/json")
                .accept("application/json")
                .body("{\"name\": \"Orange\", \"color\": \"Orange\"}")
                .when().post("/fruits")
                .then()
                .statusCode(201)
                .body(containsString("Orange"))
                .body("id", notNullValue())
                .extract().body().jsonPath().getString("id");

        //List all, Orange should be present now:
        given()
                .accept("application/json")
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        not(containsString("Cherry")),
                        containsString("Apple"),
                        containsString("Orange")
                );
    }
}
