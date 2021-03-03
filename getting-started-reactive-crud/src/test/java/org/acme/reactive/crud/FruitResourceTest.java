package org.acme.reactive.crud;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class FruitResourceTest {

    @Test
    public void testListAllFruits() {
        //List all, should have all 4 fruits the database has initially:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        containsString("Kiwi"),
                        containsString("Durian"),
                        containsString("Pomelo"),
                        containsString("Lychee"));

        //Delete the Kiwi:
        given()
                .when().delete("/fruits/1")
                .then()
                .statusCode(204);

        //List all, Kiwi should be missing now:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        not(containsString("Kiwi")),
                        containsString("Durian"),
                        containsString("Pomelo"),
                        containsString("Lychee"));
    }

}
