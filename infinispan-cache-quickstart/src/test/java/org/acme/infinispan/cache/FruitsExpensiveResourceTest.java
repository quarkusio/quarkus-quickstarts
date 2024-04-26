package org.acme.infinispan.cache;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class FruitsExpensiveResourceTest {

    @Test
    public void testFruitsEndpoint() {
        given()
              .contentType(ContentType.JSON)
              .when()
              .get("/fruits/france/paris/pear?metadata=william")
              .then()
              .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/fruits/france/paris/apple?metadata=mirabel")
                .then()
                .statusCode(200);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/fruits/france/paris/pear?metadata=william")
                .then()
                .statusCode(200);

        given()
                .when().get("/fruits/invocations")
                .then()
                .statusCode(200)
                .body(is("2"));
    }
}
