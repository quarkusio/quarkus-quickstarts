package org.acme.hibernate.orm;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class FruitsEndpointTest {

    @Test
    public void testListAllFruitsBaseSchema() {
        //List all, should have all 3 fruits the database has initially:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        containsString("Cherry"),
                        containsString("Apple"),
                        containsString("Banana"));

        //Delete the Cherry:
        given()
                .when().delete("/fruits/1")
                .then()
                .statusCode(204);

        //List all, cherry should be missing now:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        not(containsString("Cherry")),
                        containsString("Apple"),
                        containsString("Banana"));

        //Create the Pear:
        given()
                .when()
                .body("{\"name\" : \"Pear\"}")
                .contentType("application/json")
                .post("/fruits")
                .then()
                .statusCode(201);

        //List all, cherry should be missing now:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        not(containsString("Cherry")),
                        containsString("Apple"),
                        containsString("Banana"),
                        containsString("Pear"));
    }

    @Test
    public void testListAllFruitsMyCompanySchema() {
        //List all, should have all 3 fruits the database has initially:
        given()
                .when().get("/mycompany/fruits")
                .then()
                .statusCode(200)
                .body(
                        containsString("Avocado"),
                        containsString("Apricots"),
                        containsString("Blackberries"));

        //Delete the Cherry:
        given()
                .when().delete("/mycompany/fruits/1")
                .then()
                .statusCode(204);

        //List all, Avocado should be missing now:
        given()
                .when().get("/mycompany/fruits")
                .then()
                .statusCode(200)
                .body(
                        not(containsString("Avocado")),
                        containsString("Apricots"),
                        containsString("Blackberries"));

        //Create the Clementine:
        given()
                .when()
                .body("{\"name\" : \"Clementine\"}")
                .contentType("application/json")
                .post("/mycompany/fruits")
                .then()
                .statusCode(201);

        //List all, Avocado should be missing and Clementine added now:
        given()
                .when().get("/mycompany/fruits")
                .then()
                .statusCode(200)
                .body(
                        not(containsString("Avocado")),
                        containsString("Apricots"),
                        containsString("Blackberries"),
                        containsString("Clementine"));
    }

}
