package org.acme.kafka.producer;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class QuotesResourceTest {

    @Test
    void testQuotesEventStream() {
        String body = given()
                .when()
                .post("/quotes/request")
                .then()
                .statusCode(200)
                .extract().body()
                .asString();
        assertDoesNotThrow(() -> UUID.fromString(body));
    }
}
