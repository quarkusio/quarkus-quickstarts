package org.acme.funqy;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
public class FunqyTest {
    @Test
    public void testSimpleLambdaSuccess() {
        Friend friend = new Friend("Bill");
        given()
                .contentType("application/json")
                .accept("application/json")
                .body(friend)
                .when()
                .post()
                .then()
                .statusCode(200)
                .body("message", equalTo("Hello Bill"));
    }
}
