package org.acme.jms;

import static org.hamcrest.Matchers.matchesPattern;
import static org.awaitility.Awaitility.await;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

import java.time.Duration;

@QuarkusTest
@QuarkusTestResource(ArtemisTestResource.class)
public class PriceTest {

    @Test
    public void testLastPrice() {
        await().atMost(Duration.ofSeconds(30)).untilAsserted(() -> {
            RestAssured.given()
            .when().get("/prices/last")
            .then()
            .statusCode(200)
            .body(matchesPattern("\\d+"));
        });        
    }
}
