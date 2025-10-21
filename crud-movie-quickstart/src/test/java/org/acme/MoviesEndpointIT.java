package org.acme;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusIntegrationTest
public class MoviesEndpointIT {

    @BeforeEach
    public void setUp() {
        given()
                .when().delete("/movies")
                .then()
                .statusCode(204);
    }

    @Test
    public void testGetAllInitiallyEmpty() {
        given()
                .when().get("/movies")
                .then()
                .statusCode(200)
                .body("$.size()", is(0));
    }

    @Test
    public void testCreateMovie() {
        var uri = given()
                .contentType(JSON)
                .body("{\"title\":\"Inception\",\"rating\":5}")
                .when().post("/movies")
                .then()
                .statusCode(201)
                .extract().header("Location");

        given()
                .get(uri)
                .then()
                .body("id", notNullValue())
                .body("title", is("Inception"))
                .body("rating", is(5));
    }

    @Test
    public void testGetOneAndNotFound() {
        var uri = given()
                .contentType(JSON)
                .body("{\"title\":\"Interstellar\",\"rating\":4}")
                .when().post("/movies")
                .then()
                .statusCode(201)
                .extract().header("Location");

        given()
                .when().get(uri)
                .then()
                .statusCode(200)
                .body("title", is("Interstellar"));

        given()
                .when().get("/movies/9999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testUpdateMovie() {
        var uri = given()
                .contentType(JSON)
                .body("{\"title\":\"Old Title\",\"rating\":2}")
                .when().post("/movies")
                .then()
                .statusCode(201)
                .extract().header("Location");

        given()
                .contentType(JSON)
                .body("{\"title\":\"New Title\",\"rating\":3}")
                .when().patch(uri)
                .then()
                .statusCode(200)
                .body("title", is("New Title"))
                .body("rating", is(3));
    }

    @Test
    public void testUpdateNotFound() {
        given()
                .contentType(JSON)
                .body("{\"title\":\"Doesn't Matter\",\"rating\":1}")
                .when().patch("/movies/9999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteMovie() {
        var uri = given()
                .contentType(JSON)
                .body("{\"title\":\"To be deleted\",\"rating\":1}")
                .when().post("/movies")
                .then()
                .statusCode(201)
                .extract().header("Location");

        given()
                .when().delete(uri)
                .then()
                .statusCode(204);

        given()
                .when().get(uri)
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteNotFound() {
        given()
                .when().delete("/movies/9999")
                .then()
                .statusCode(404);
    }

    @Test
    void helloEndpoint() {
        given()
                .get("/movies/hello")
                .then()
                .statusCode(200)
                .contentType("text/plain")
                .body(is("OK"));
    }

    @Test
    void shouldNotAddInvalidItem() {
        var movie = new Movie();
        movie.title = null;
        movie.rating = 2;

        given()
                .when()
                .body(movie)
                .contentType(JSON)
                .accept(JSON)
                .post("/movies")
                .then()
                .statusCode(400);
    }

    @Test
    void shouldNotAddNullItem() {
        given()
                .when()
                .contentType(JSON)
                .accept(JSON)
                .post("/movies")
                .then()
                .statusCode(400);
    }



}