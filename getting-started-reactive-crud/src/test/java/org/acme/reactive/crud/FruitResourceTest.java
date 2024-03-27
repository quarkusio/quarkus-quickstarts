package org.acme.reactive.crud;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FruitResourceTest {
    private static String newFruit = "Lemon";
    private static String updatedFruit = "Pitaya";

    @Test
    @Order(1)
    public void testAddFruit() {
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

        // Add new fruit
        given()
                .when().contentType("application/json")
                .body("{\"name\":\"" + newFruit + "\"}")
                .post("/fruits")
                .then()
                .statusCode(201);

        //List all, should have 4 fruits the database has initially and 1 added fruit:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        containsString("Kiwi"),
                        containsString("Durian"),
                        containsString("Pomelo"),
                        containsString("Lychee"),
                        containsString(newFruit));
    }

    @Test
    @Order(2)
    public void testUpdateFruit() {
        //List all, should have 4 fruits the database has initially and 1 added fruit:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        containsString("Kiwi"),
                        containsString("Durian"),
                        containsString("Pomelo"),
                        containsString("Lychee"),
                        containsString(newFruit));

        // Update name of added fruit
        given()
                .when().contentType("application/json")
                .body("{\"id\":5, \"name\":\"" + updatedFruit + "\"}")
                .put("/fruits/5")
                .then()
                .statusCode(200);

        //List all, should have 4 fruits the database has initially and 1 updated fruit:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        containsString("Kiwi"),
                        containsString("Durian"),
                        containsString("Pomelo"),
                        containsString("Lychee"),
                        containsString(updatedFruit));
    }

    @Test
    @Order(3)
    public void testListAllFruits() {
        //List all, should have 5 fruits the database has initially:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        containsString("Kiwi"),
                        containsString("Durian"),
                        containsString("Pomelo"),
                        containsString("Lychee"),
                        containsString(updatedFruit));

        //Delete the Pitaya:
        given()
                .when().delete("/fruits/5")
                .then()
                .statusCode(204);

        //List all, Pitaya should be missing now:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        containsString("Kiwi"),
                        containsString("Durian"),
                        containsString("Pomelo"),
                        containsString("Lychee"),
                        not(containsString(updatedFruit)));
    }

    @Test
    public void testNotFoundForDelete() {
        given()
                .when().delete("/fruits/3939")
                .then()
                .statusCode(404);
    }

    @Test
    public void testNotFoundForUpdate() {
        given()
                .when().contentType("application/json")
                .body("{\"id\":3939, \"name\":\"" + updatedFruit + "\"}")
                .put("/fruits/3939")
                .then()
                .statusCode(404);
    }

}
