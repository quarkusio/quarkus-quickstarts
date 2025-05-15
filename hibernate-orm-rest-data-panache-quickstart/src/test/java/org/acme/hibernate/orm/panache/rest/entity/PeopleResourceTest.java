package org.acme.hibernate.orm.panache.rest.entity;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class PeopleResourceTest {
    @Test
    void shouldListPersons() {
        given().accept(ContentType.JSON)
                .when().get("/my-people/all")
                .then().statusCode(200)
                .and().body("id", contains(1, 2))
                .and().body("name", contains("John Johnson", "Peter Peterson"));
    }

    @Test
    void shouldListPersonsAndHal() {
        given().accept("application/hal+json")
                .when().get("/my-people/all")
                .then().statusCode(200).log().all()
                .and().body("_embedded.people.id", contains(1, 2))
                .and().body("_embedded.people._links.add.href",
                        contains(endsWith("/my-people"), endsWith("/my-people")))
                .and().body("_embedded.people._links.count.href",
                        contains(endsWith("/my-people/count"), endsWith("/my-people/count")))
                .and().body("_embedded.people._links.self.href",
                        contains(endsWith("/my-people/1"), endsWith("/my-people/2")))
                .and().body("_embedded.people._links.update.href",
                        contains(endsWith("/my-people/1"), endsWith("/my-people/2")))
                .and().body("_embedded.people._links.list.href",
                        contains(endsWith("/my-people/all"), endsWith("/my-people/all")));
    }

    @Test
    void shouldListPersonsInAscending() {
        given().accept(ContentType.JSON)
                .when().get("/my-people/all?sort=name,id")
                .then().statusCode(200)
                .and().body("id", contains(1, 2))
                .and().body("name", contains("John Johnson", "Peter Peterson"));
    }

    @Test
    void shouldListPersonsInDescending() {
        given().accept(ContentType.JSON)
                .when().get("/my-people/all?sort=-name,id")
                .then().statusCode(200)
                .and().body("id", contains(2, 1))
                .and().body("name", contains("Peter Peterson", "John Johnson"));
    }

    @Test
    void shouldListPersonsWithPageAndSize() {
        given().accept(ContentType.JSON)
                 .and().queryParam("page", 0)
                 .and().queryParam("size", 1)
                 .when().get("/my-people/all?sort=name")
                 .then().statusCode(200)
                 .and().body("id", contains(1))
                 .and().body("name", contains("John Johnson"));
    }

    @Test
    void shouldListPersonsWithFieldFilter() {
        given().accept(ContentType.JSON)
                .when()
                .queryParam("name", "John Johnson")
                .when().get("/my-people/all")
                .then().statusCode(200)
                .and().body("id", contains(1))
                .and().body("name", contains("John Johnson"));
    }

    @Test
    void shouldListPersonsWithNamedFilter() {
        given().accept(ContentType.JSON)
                .queryParam("name", "John")
                .queryParam("namedQuery", "PersonForEntity.containsInName")
                .when().get("/my-people/all")
                .then().statusCode(200)
                .and().body("id", contains(1))
                .and().body("name", contains("John Johnson"));
    }

    @Test
    void shouldGetTotalNumberOfEntities() {
        given().get("/my-people/count")
                .then().statusCode(HttpStatus.SC_OK)
                .and().body(is(equalTo("2")));
    }

    @Test
    void shouldGetPerson() {
        given().accept(ContentType.JSON)
                .when().get("/my-people/1")
                .then().statusCode(200)
                .and().body("id", is(equalTo(1)))
                .and().body("name", is(equalTo("John Johnson")));
    }

    @Test
    void shouldDeleteNotBeExposed() {
        given().when().delete("/my-people/1")
                .then().statusCode(405);
    }

    @Test
    void shouldUpdatePerson() {
        given().accept(ContentType.JSON)
                .when().get("/my-people/1")
                .then().statusCode(200)
                .and().body("id", is(equalTo(1)))
                .and().body("name", is(equalTo("John Johnson")));

        Person newPerson = new Person();
        newPerson.id = 1L;
        newPerson.name = "John Johnson";

        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("zipcode", 95014);
        jsonObject.put("city", "cupertino");
        newPerson.jsonAddress = jsonObject;

        given().accept(ContentType.JSON)
                .and().contentType(ContentType.JSON)
                .and().body(newPerson)
                .when().put("/my-people/1").then().statusCode(204);

        // verify after updating that the new json address fields were stored
        given().accept(ContentType.JSON)
                .when().get("/my-people/1")
                .then().statusCode(200)
                .and().body("id", is(equalTo(1)))
                .and().body("name", is(equalTo("John Johnson")))
                .and().body("jsonAddress", is(equalTo(jsonObject)));
    
    }

}
