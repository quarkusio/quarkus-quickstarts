package org.acme.mongodb.panache;

import static io.restassured.config.LogConfig.logConfig;

import java.time.LocalDate;

import org.acme.mongodb.panache.repository.Person;
import org.acme.mongodb.panache.repository.Status;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;

@QuarkusTest
@QuarkusTestResource(MongoDbResource.class)
class PersonResourceTest {

    @BeforeAll
    static void initAll() {
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.config
                .logConfig((logConfig().enableLoggingOfRequestAndResponseIfValidationFails()))
                .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((type, s) -> new ObjectMapper()
                        .registerModule(new Jdk8Module())
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)));
    }

    @Test
    void testEntity() {
        performTest("/entity/persons");
    }

    @Test
    void testRepository() {
        performTest("/repository/persons");
    }

    private void performTest(String path) {
        String person1 = "{ \"name\" : \"moncef\", \"birthDate\" : \"1993-05-19\", \"status\" : \"LIVING\"}";
        String person2 = "{ \"name\" : \"loïc\", \"birthDate\" : \"1988-06-19\", \"status\" : \"LIVING\"}";
        String person3 = "{ \"name\" : \"foo\", \"birthDate\" : \"1993-05-18\", \"status\" : \"DECEASED\"}";
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(person1)
                .when()
                .post(path)
                .then()
                .statusCode(201);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(person2)
                .when()
                .post(path)
                .then()
                .statusCode(201);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(person3)
                .when()
                .post(path)
                .then()
                .statusCode(201);

        Person[] persons = RestAssured.given()
                .when()
                .contentType(ContentType.JSON)
                .get(path)
                .then()
                .statusCode(200)
                .extract()
                .body().as(Person[].class);

        Assertions.assertThat(persons.length).isEqualTo(3);

        System.out.println("ObjectId: " + persons[0].id.toString());
        Person person = RestAssured
                .given()
                .when()
                .contentType(ContentType.JSON)
                .get(path + "/{id}", persons[0].id.toString())
                .then()
                .statusCode(200)
                .extract()
                .body().as(Person.class);

        Assertions.assertThat(person.id).isEqualTo(persons[0].id);
        Assertions.assertThat(person.name).isEqualTo(persons[0].name);
        Assertions.assertThat(person.birthDate).isEqualTo(persons[0].birthDate);
        Assertions.assertThat(person.status).isEqualTo(persons[0].status);

        person = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get(path + "/search/{name}", "moncef")
                .then()
                .statusCode(200)
                .extract()
                .body().as(Person.class);

        Assertions.assertThat(person.name).isEqualTo("MONCEF");
        Assertions.assertThat(person.birthDate).isEqualTo(LocalDate.of(1993, 5, 19));
        Assertions.assertThat(person.status).isEqualTo(Status.LIVING);


        RestAssured
                .given()
                .when()
                .contentType(ContentType.JSON)
                .delete(path + "/{id}", person.id.toString())
                .then()
                .statusCode(204);

        persons = RestAssured.given()
                .when()
                .contentType(ContentType.JSON)
                .get(path)
                .then()
                .statusCode(200)
                .extract()
                .body().as(Person[].class);

        Assertions.assertThat(persons.length).isEqualTo(2);

        RestAssured
                .given()
                .when()
                .contentType(ContentType.JSON)
                .delete(path)
                .then()
                .statusCode(204);
    }
}
