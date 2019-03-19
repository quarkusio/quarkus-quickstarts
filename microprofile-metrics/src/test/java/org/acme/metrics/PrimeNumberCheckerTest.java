package org.acme.metrics;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Header;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PrimeNumberCheckerTest {

    @Test
    public void checkCounter() {
        get("/31");
        assertMetricValue("org.acme.metrics.PrimeNumberChecker.performedChecks", 1);
        get("/33");
        assertMetricValue("org.acme.metrics.PrimeNumberChecker.performedChecks", 2);
        get("/887");
        assertMetricValue("org.acme.metrics.PrimeNumberChecker.highestPrimeNumberSoFar", 887);
        get("/900");
        assertMetricValue("org.acme.metrics.PrimeNumberChecker.highestPrimeNumberSoFar", 887);
    }


    private void assertMetricValue(String metric, Object value) {
        given().header(new Header("Accept", "application/json"))
                .get("/metrics/application").then()
                .statusCode(200)
                .body("'" + metric + "'", is(value));
    }

}
