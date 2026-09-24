package org.acme;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class PoemsTest {

    @Test
    public void testPoemsEndpoint() {
        given()
                .when().get("/poems")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }

}
