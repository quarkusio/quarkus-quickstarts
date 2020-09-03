package org.acme.webapp;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testGreetingEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .contentType(MediaType.TEXT_PLAIN)
                .body(equalTo("hello"));
    }

    @Test
    public void testGreetingEndpointWithName() {
        given()
                .when().get("/hello/john")
                .then()
                .statusCode(200)
                .contentType(MediaType.TEXT_PLAIN)
                .body(equalTo("hello john"));
    }
}
