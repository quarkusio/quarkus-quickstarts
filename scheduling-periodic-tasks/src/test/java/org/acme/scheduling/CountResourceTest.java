package org.acme.scheduling;

import org.jboss.shamrock.test.ShamrockTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;;

@RunWith(ShamrockTest.class)
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
