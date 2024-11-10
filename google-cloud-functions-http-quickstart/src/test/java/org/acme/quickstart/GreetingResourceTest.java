package org.acme.quickstart;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        when().get("/hello").then().statusCode(200);

        when().get("/servlet/hello").then().statusCode(200);

        when().get("/vertx/hello").then().statusCode(200);

        when().get("/funqy").then().statusCode(200);
    }

}