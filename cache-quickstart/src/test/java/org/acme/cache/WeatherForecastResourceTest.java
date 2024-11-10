package org.acme.cache;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class WeatherForecastResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/weather")
                .then()
                .statusCode(200)
                .body(containsString("dailyForecasts"));
    }

}
