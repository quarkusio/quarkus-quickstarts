package org.acme.hibernate.reactive;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

@QuarkusTest
public class FruitsEndpointTest {

    private static final String BASE_URL = "/fruits/";
    private static final String APP_JSON = "application/json";

    @Test
    public void testListAllFruits() {
        //List all, should have all 3 fruits the database has initially:
        getHelper()
                .body(
                        containsString("Cherry"),
                        containsString("Apple"),
                        containsString("Banana"));

        // Update Cherry to Pineapple
        given()
                .when()
                .body("{\"name\" : \"Pineapple\"}")
                .contentType(APP_JSON)
                .put(BASE_URL + "1")
                .then()
                .statusCode(200)
                .body(
                        containsString("\"id\":"),
                        containsString("\"name\":\"Pineapple\""));

        //List all, Pineapple should've replaced Cherry:
        getHelper()
                .body(
                        not(containsString("Cherry")),
                        containsString("Pineapple"),
                        containsString("Apple"),
                        containsString("Banana"));

        //Delete Pineapple:
        given()
                .when()
                .delete(BASE_URL + "1")
                .then()
                .statusCode(204);

        //List all, Pineapple should be missing now:
        getHelper()
                .body(
                        not(containsString("Pineapple")),
                        containsString("Apple"),
                        containsString("Banana"));

        //Create the Pear:
        given()
                .when()
                .body("{\"name\" : \"Pear\"}")
                .contentType(APP_JSON)
                .post(BASE_URL)
                .then()
                .statusCode(201)
                .body(
                        containsString("\"id\":"),
                        containsString("\"name\":\"Pear\""));

        //List all, Pineapple should be still missing now:
        getHelper()
                .body(
                        not(containsString("Pineapple")),
                        containsString("Apple"),
                        containsString("Banana"),
                        containsString("Pear"));
    }


    private ValidatableResponse getHelper() {
        return given()
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(200);
    }
}
