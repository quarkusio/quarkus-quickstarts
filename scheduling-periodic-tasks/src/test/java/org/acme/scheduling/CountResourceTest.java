package org.acme.scheduling;

import org.jboss.shamrock.test.junit.ShamrockTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;;

@ShamrockTest
public class CountResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/count")
          .then()
             .statusCode(200)
             .body(containsString("count"));
    }

}
