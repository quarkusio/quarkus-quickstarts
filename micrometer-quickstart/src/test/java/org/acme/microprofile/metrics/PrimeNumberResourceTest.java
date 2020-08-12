package org.acme.microprofile.metrics;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Header;

@QuarkusTest
public class PrimeNumberResourceTest {

    @Test
    public void checkCounter() {
        get("/31");
        get("/33");
        get("/887");
        get("/900");

        // Prometheus results have all of the data. We're checking for:
        // 4 successful (200) requests to /{number}
        // 3 attempts at non-trivial detection of a prime number
        // max prime seen
        given().get("/metrics").then()
                .statusCode(200)
                .log().all()
                .body(containsString("prime_number_max 887.0"))
                .body(containsString("prime_number_test_seconds_count 3.0"))
                .body(containsString("http_server_requests_seconds_count{method=\"GET\",outcome=\"SUCCESS\",status=\"200\",uri=\"/{number}\",} 4.0"));
    }
}
