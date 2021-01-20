package org.acme.funqy;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class FunqyTest {

    @Test
    public void testDefaultChain() {
        RestAssured.given().contentType("application/json")
                .header("ce-specversion", "1.0")
                .header("ce-id", UUID.randomUUID().toString())
                .header("ce-type", "defaultChain")
                .header("ce-source", "test")
                .body("\"Start\"")
                .post("/")
                .then().statusCode(200)
                .header("ce-id", notNullValue())
                .header("ce-type", "defaultChain.output")
                .header("ce-source", "defaultChain")
                .body(Matchers.equalTo("\"Start::defaultChain\""));
    }

    @Test
    public void testConfigChain() {
        RestAssured.given().contentType("application/json")
                .header("ce-specversion", "1.0")
                .header("ce-id", UUID.randomUUID().toString())
                .header("ce-type", "defaultChain.output")
                .header("ce-source", "test")
                .body("\"Start::defaultChain\"")
                .post("/")
                .then().statusCode(200)
                .header("ce-id", notNullValue())
                .header("ce-type", "annotated")
                .header("ce-source", "configChain")
                .body(Matchers.equalTo("\"Start::defaultChain::configChain\""));
    }

    @Test
    public void testAnnotatedChain() {
        RestAssured.given().contentType("application/json")
                .header("ce-specversion", "1.0")
                .header("ce-id", UUID.randomUUID().toString())
                .header("ce-type", "annotated")
                .header("ce-source", "test")
                .body("\"Start::defaultChain::configChain\"")
                .post("/")
                .then().statusCode(200)
                .header("ce-id", notNullValue())
                .header("ce-type", "lastChainLink")
                .header("ce-source", "annotated")
                .body(Matchers.equalTo("\"Start::defaultChain::configChain::annotatedChain\""));
    }


}
