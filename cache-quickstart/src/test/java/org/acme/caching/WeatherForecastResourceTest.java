package org.acme.caching;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

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
