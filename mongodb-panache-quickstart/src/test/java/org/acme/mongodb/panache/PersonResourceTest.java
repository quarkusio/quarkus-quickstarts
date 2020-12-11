package org.acme.mongodb.panache;

import static io.restassured.config.LogConfig.logConfig;
import static org.acme.mongodb.panache.MongoDbContainer.MONGODB_HOST;
import static org.acme.mongodb.panache.MongoDbContainer.MONGODB_PORT;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;

@Testcontainers
@QuarkusTest
class PersonResourceTest {

    @Container
    static GenericContainer MONGO_DB_CONTAINER = new MongoDbContainer()
            .withCreateContainerCmdModifier(cmd -> cmd.withHostName(MONGODB_HOST)
                    .withPortBindings(new PortBinding(Ports.Binding.bindPort(MONGODB_PORT), new ExposedPort(MONGODB_PORT))));

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
    void test() {
        String person1 = "{ \"id\" : \"5889273c093d1c3e614bf2f9\", \"name\" : \"moncef\", \"birthDate\" : \"1993-05-19\", \"status\" : \"LIVING\"}";
        String person2 = "{ \"id\" : \"5889273c093d1c3e614bf2fa\", \"name\" : \"loïc\", \"birthDate\" : \"1988-06-19\", \"status\" : \"LIVING\"}";
        String person3 = "{ \"id\" : \"5889273c093d1c3e614bf2fb\", \"name\" : \"foo\", \"birthDate\" : \"1993-05-18\", \"status\" : \"DECEASED\"}";
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(person1)
                .when()
                .post("/persons")
                .then()
                .statusCode(201);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(person2)
                .when()
                .post("/persons")
                .then()
                .statusCode(201);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(person3)
                .when()
                .post("/persons")
                .then()
                .statusCode(201);

        Person[] persons = RestAssured.given()
                .when()
                .contentType(ContentType.JSON)
                .get("/persons")
                .then()
                .statusCode(200)
                .extract()
                .body().as(Person[].class);

        Assertions.assertThat(persons.length).isEqualTo(3);

        Person person = RestAssured
                .given()
                .when()
                .contentType(ContentType.JSON)
                .get("/persons/{id}", "5889273c093d1c3e614bf2fa")
                .then()
                .statusCode(200)
                .extract()
                .body().as(Person.class);

        Assertions.assertThat(person.id).isEqualTo(new ObjectId("5889273c093d1c3e614bf2fa"));
        Assertions.assertThat(person.name).isEqualTo("loïc");
        Assertions.assertThat(person.birthDate).isEqualTo(LocalDate.of(1988, 6, 19));
        Assertions.assertThat(person.status).isEqualTo(Status.LIVING);

        String personDto = "{ \"id\" : \"5889273c093d1c3e614bf2fc\", \"name\" : \"jugo\", \"birthDate\" : \"1964-10-31\", \"status\" : \"DECEASED\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(personDto)
                .when()
                .post("/persons")
                .then()
                .statusCode(201);

        personDto = "{ \"id\" : \"5889273c093d1c3e614bf2fb\", \"name\" : \"LHez\"}";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(personDto)
                .when()
                .put("/persons/{id}", "5889273c093d1c3e614bf2fa")
                .then()
                .statusCode(204);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/persons/{id}", "5889273c093d1c3e614bf2fb")
                .then()
                .statusCode(204);

        person = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .get("/persons/search/{name}", "moncef")
                .then()
                .statusCode(200)
                .extract()
                .body().as(Person.class);

        Assertions.assertThat(person.id).isEqualTo(new ObjectId("5889273c093d1c3e614bf2f9"));
        Assertions.assertThat(person.name).isEqualTo("moncef");
        Assertions.assertThat(person.birthDate).isEqualTo(LocalDate.of(1993, 5, 19));
        Assertions.assertThat(person.status).isEqualTo(Status.LIVING);

        Long count = RestAssured
                .given()
                .when()
                .contentType(ContentType.JSON)
                .get("/persons/count")
                .then()
                .statusCode(200)
                .extract()
                .body().as(Long.class);

        Assertions.assertThat(count).isGreaterThan(0);
    }
}
