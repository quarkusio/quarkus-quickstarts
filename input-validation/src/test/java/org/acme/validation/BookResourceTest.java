package org.acme.validation;

import org.jboss.shamrock.test.ShamrockTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@RunWith(ShamrockTest.class)
public class BookResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/books")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

    @Test
    public void testValidBook() {
        given()
            .body("{\"title\": \"some book\", \"author\": \"me\", \"pages\":5}")
            .header("Content-Type", "application/json")
        .when()
            .post("/books/manual-validation")
        .then()
            .statusCode(200)
            .body("success", is(true), "message", containsString("Book is valid!"));
    }

    @Test
    public void testBookWithoutTitle() {
        given()
            .body("{\"author\": \"me\", \"pages\":5}")
            .header("Content-Type", "application/json")
        .when()
            .post("/books/manual-validation")
        .then()
            .statusCode(400)
            .body("success", is(false), "message", containsString("Title"));
    }

    @Test
    public void testBookWithoutAuthor() {
        given()
            .body("{\"title\": \"catchy\", \"pages\":5}")
            .header("Content-Type", "application/json")
        .when()
            .post("/books/manual-validation")
        .then()
            .statusCode(400)
            .body("success", is(false), "message", containsString("Author"));
    }

    @Test
    public void testBookWithNegativePage() {
        given()
            .body("{\"title\": \"some book\", \"author\": \"me\", \"pages\":-25}")
            .header("Content-Type", "application/json")
        .when()
            .post("/books/manual-validation")
        .then()
            .statusCode(400)
            .body("success", is(false), "message", containsString("lazy"));
    }

    @Test
    public void testValidBookEndPointValidation() {
        given()
            .body("{\"title\": \"some book\", \"author\": \"me\", \"pages\":5}")
            .header("Content-Type", "application/json")
        .when()
            .post("/books/end-point-method-validation")
        .then()
            .statusCode(200)
            .body("success", is(true), "message", containsString("Book is valid!"));
    }

    @Test
    public void testBookWithoutTitleEndPointValidation() {
        given()
            .body("{\"author\": \"me\", \"pages\":5}")
            .header("Content-Type", "application/json")
        .when()
            .post("/books/end-point-method-validation")
        .then()
            .statusCode(400)
            .body("success", is(false), "message", containsString("Title"));
    }

    @Test
    public void testValidBookServiceValidation() {
        given()
            .body("{\"title\": \"some book\", \"author\": \"me\", \"pages\":5}")
            .header("Content-Type", "application/json")
        .when()
            .post("/books/service-method-validation")
        .then()
            .statusCode(200)
            .body("success", is(true), "message", containsString("Book is valid!"));
    }

    @Test
    public void testBookWithoutTitleServiceValidation() {
        given()
            .body("{\"author\": \"me\", \"pages\":5}")
            .header("Content-Type", "application/json")
        .when()
            .post("/books/service-method-validation")
        .then()
            .statusCode(400)
            .body("success", is(false), "message", containsString("Title"));
    }
}
