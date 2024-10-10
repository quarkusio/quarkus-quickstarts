package org.acme.hibernate.reactive.panache.rest.repository;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PeopleResourceTest {

    @Order(1)
    @Test
    void shouldListPersons() {
        given().accept(ContentType.JSON)
                .when().get("/people")
                .then().statusCode(200)
                .and().body("id", contains(1, 2))
                .and().body("name", contains("John Johnson", "Peter Peterson"));
    }

    @Order(2)
    @Test
    void shouldGetTotalNumberOfEntities() {
        given().get("/people/count")
                .then().statusCode(HttpStatus.SC_OK)
                .and().body(is(equalTo("2")));
    }

    @Order(3)
    @Test
    void shouldGetPerson() {
        given().accept(ContentType.JSON)
                .when().get("/people/1")
                .then().statusCode(200)
                .and().body("id", is(equalTo(1)))
                .and().body("name", is(equalTo("John Johnson")));
    }

    @Order(4)
    @Test
    void shouldCreateNewPerson() {
        Person newPerson = new Person();
        newPerson.name = "Jefferson";
        newPerson.birthDate = LocalDate.of(1985, 12, 1);

        given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .and().body(newPerson)
                .when().post("/people")
                .then()
                .statusCode(201)
                .and()
                .body("id", is(equalTo(3)));
    }

    @Order(5)
    @Test
    void shouldUpdatePerson() {
        Person newPerson = new Person();
        newPerson.name = "Holly";
        newPerson.birthDate = LocalDate.of(2001, 11, 20);

        given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .and().body(newPerson)
                .when().put("/people/1")
                .then()
                .statusCode(204);
    }

    @Order(6)
    @Test
    void shouldDeletePerson() {
        when().delete("/people/1")
                .then().statusCode(204);
    }
}
