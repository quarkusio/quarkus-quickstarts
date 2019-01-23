package org.acme.spring.di;

import org.jboss.shamrock.test.junit.ShamrockTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@ShamrockTest
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
            .when().get("/greeting")
            .then()
                .statusCode(200)
                .body(is("HELLO WORLD!"));
    }

}
