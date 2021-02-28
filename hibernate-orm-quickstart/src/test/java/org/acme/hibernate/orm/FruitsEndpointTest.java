package org.acme.hibernate.orm;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class FruitsEndpointTest {

    private static final String BASE_URL = "/fruits";

    @Test
    public void testListAllFruits() {
        //List all, should have all 3 fruits the database has initially:
       getHelper()
                .body(
                        containsString("Cherry"),
                        containsString("Apple"),
                        containsString("Banana"));

        //Delete the Cherry:
        given()
                .when().delete(BASE_URL + "/1")
                .then()
                .statusCode(204);

        //List all, cherry should be missing now:
        getHelper()
                .body(
                        not(containsString("Cherry")),
                        containsString("Apple"),
                        containsString("Banana"));

        //Create the Pear:
        given()
                .when()
                .body("{\"name\" : \"Pear\"}")
                .contentType(APPLICATION_JSON)
                .post(BASE_URL)
                .then()
                .statusCode(201);

        //List all, cherry should be missing now:
        getHelper()
                .body(
                        not(containsString("Cherry")),
                        containsString("Apple"),
                        containsString("Banana"),
                        containsString("Pear"));
    }

    private ValidatableResponse getHelper() {
        return given()
                .when().get(BASE_URL)
                .then()
                .statusCode(200);
    }
}
