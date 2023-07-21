package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static java.util.regex.Pattern.matches;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(containsString("Hello"));
    }

}