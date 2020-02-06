package org.acme.jms;

import static org.hamcrest.Matchers.matchesPattern;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

@QuarkusTest
@QuarkusTestResource(ArtemisTestResource.class)
public class PriceTest {

    @Test
    public void testLastPrice() throws InterruptedException {
        Thread.sleep(100L);
        RestAssured.given()
                .when().get("/prices/last")
                .then()
                .statusCode(200)
                .body(matchesPattern("\\d+"));
    }
}
