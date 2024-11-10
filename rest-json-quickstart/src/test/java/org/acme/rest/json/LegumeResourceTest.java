package org.acme.rest.json;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class LegumeResourceTest {

    @Test
    public void testList() {
        given()
                .when().get("/legumes")
                .then()
                .statusCode(200)
                .body("$.size()", is(2),
                        "name", containsInAnyOrder("Carrot", "Zucchini"),
                        "description", containsInAnyOrder("Root vegetable, usually orange", "Summer squash"));
    }
}
