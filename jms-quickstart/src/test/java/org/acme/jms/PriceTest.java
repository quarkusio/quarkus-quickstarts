package org.acme.jms;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.matchesPattern;

@QuarkusTest
@QuarkusTestResource(ArtemisTestResource.class)
public class PriceTest {

    @Test
    public void testLastPrice() throws InterruptedException {
        await().atMost(Duration.ofSeconds(30))
                .untilAsserted(() -> {
                    RestAssured.given()
                            .when().get("/prices/last")
                            .then()
                            .statusCode(200)
                            .body(matchesPattern("\\d+"));
                });
    }
}
