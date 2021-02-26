package org.acme.hibernate.orm;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;

@QuarkusTest
public class FruitsEndpointTest {

    @Test
    public void testListAllFruitsBaseSchema() {

        // Ensure initial state for multiple runs
        Fruit cherry = findOrCreate("", "Cherry");
        deleteIfExist("", "Pear");

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
                .when().delete("/fruits/" + cherry.getId())
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

        // Ensure initial state for multiple runs
        Fruit avocado = findOrCreate("/mycompany", "Avocado");
        deleteIfExist("/mycompany", "Clementine");

        //List all, should have all 3 fruits the database has initially:
        given()
                .when().get("/mycompany/fruits")
                .then()
                .statusCode(200)
                .body(
                        containsString("Avocado"),
                        containsString("Apricots"),
                        containsString("Blackberries"));

        //Delete the Avocado:
        given()
                .when().delete("/mycompany/fruits/" + avocado.getId())
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

    private void create(String tenantPrefix, String fruitName) {
        given()
                .when()
                .body("{\"name\" : \"" + fruitName + "\"}")
                .contentType("application/json")
                .post(tenantPrefix + "/fruits")
                .then()
                .statusCode(201);
    }

    private void deleteIfExist(String tenantPrefix, String fruitName) {
        Response response = given().param("type", "name").param("value", fruitName).when().get(tenantPrefix + "/fruitsFindBy");
        if (response.statusCode() == 200) {
            Fruit fruit = response.as(Fruit.class);
            given()
                    .when().delete(tenantPrefix + "/fruits/" + fruit.getId())
                    .then()
                    .statusCode(204);
        }
    }

    private Fruit find(String tenantPrefix, String fruitName) {
        Response response = given().param("type", "name").param("value", fruitName).when().get(tenantPrefix + "/fruitsFindBy");
        if (response.statusCode() == 404) {
            return null;
        }
        if (response.statusCode() == 200) {
            Fruit fruit = response.as(Fruit.class);
            return fruit;
        }
        throw new IllegalStateException("Unknown status finding '" + fruitName + ": " + response);
    }

    private Fruit findOrCreate(String tenantPrefix, String name) {
        Fruit fruit = find(tenantPrefix, name);
        if (fruit == null) {
            create(tenantPrefix, name);
            return find(tenantPrefix, name);
        }
        return fruit;
    }

}
