package org.acme.rest.client;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CountriesResourceTest {

    @Test
    public void testCountryNameEndpoint() {
        given()
                .when().get("/country/name/greece")
                .then()
                .statusCode(200)
                .body("$.size()", is(1),
                        "[0].alpha2Code", is("GR"),
                        "[0].capital", is("Athens"),
                        "[0].currencies.size()", is(1),
                        "[0].currencies[0].name", is("Euro"));
    }

    @Test
    public void testCountryNameAsyncEndpoint() {
        given()
                .when().get("/country/name-async/greece")
                .then()
                .statusCode(200)
                .body("$.size()", is(1),
                        "[0].alpha2Code", is("GR"),
                        "[0].capital", is("Athens"),
                        "[0].currencies.size()", is(1),
                        "[0].currencies[0].name", is("Euro"));
    }

    @Test
    public void testCountryNameMutinyEndpoint() {
        given()
                .when().get("/country/name-uni/greece")
                .then()
                .statusCode(200)
                .body("$.size()", is(1),
                        "[0].alpha2Code", is("GR"),
                        "[0].capital", is("Athens"),
                        "[0].currencies.size()", is(1),
                        "[0].currencies[0].name", is("Euro"));
    }
}
