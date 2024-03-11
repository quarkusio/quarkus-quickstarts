package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.comparesEqualTo;

@QuarkusTest
public class CostAppTest {

    @Test
    public void testCostPassingThrough() {
        given().body("{\"value\": 10.0, \"currency\": \"PLN\"}")
          .when().post("/costs")
          .then()
             .statusCode(202);
        await("cost added")
                .atMost(10, TimeUnit.SECONDS)
                .until(() -> {
                    String resultAsString = get("/cost-collector").getBody().asString();
                    return Double.valueOf(resultAsString);
                }, comparesEqualTo(2.2));
    }

}