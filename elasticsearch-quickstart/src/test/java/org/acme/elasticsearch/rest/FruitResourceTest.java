package org.acme.elasticsearch.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.acme.elasticsearch.Fruit;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

@QuarkusTest
class FruitResourceTest {

    @Test
    void test() {
        Fruit fruit = new Fruit();
        fruit.name = "bananas";
        fruit.color = "yellow";

        given().body(fruit).contentType(ContentType.JSON)
            .when().post("/java/fruits")
            .then().statusCode(201);

        when().get("/java/fruits/search?color=yellow")
            .then().statusCode(200);
    }
}