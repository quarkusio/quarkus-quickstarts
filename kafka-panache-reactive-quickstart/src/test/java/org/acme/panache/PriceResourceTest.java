package org.acme.panache;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
class PriceResourceTest {

    @Test
    void testPrices() {
        await().untilAsserted(() -> {
            given()
                    .when().get("/prices")
                    .then()
                    .statusCode(200)
                    .body("size()", greaterThanOrEqualTo(1));

        });

    }
}