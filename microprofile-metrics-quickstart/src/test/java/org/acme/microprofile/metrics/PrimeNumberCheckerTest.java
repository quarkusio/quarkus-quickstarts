package org.acme.microprofile.metrics;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Header;

@QuarkusTest
public class PrimeNumberCheckerTest {

    @Test
    public void checkCounter() {
        get("/31");
        assertMetricValue("org.acme.microprofile.metrics.PrimeNumberChecker.performedChecks", 1);
        get("/33");
        assertMetricValue("org.acme.microprofile.metrics.PrimeNumberChecker.performedChecks", 2);
        get("/887");
        assertMetricValue("org.acme.microprofile.metrics.PrimeNumberChecker.highestPrimeNumberSoFar", 887);
        get("/900");
        assertMetricValue("org.acme.microprofile.metrics.PrimeNumberChecker.highestPrimeNumberSoFar", 887);
    }

    private void assertMetricValue(String metric, Object value) {
        given().header(new Header("Accept", "application/json"))
                .get("/q/metrics/application").then()
                .statusCode(200)
                .body("'" + metric + "'", is(value));
    }

}
