package org.acme.infinispan.client;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class InfinispanGreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
              .contentType(ContentType.JSON)
              .body("{\"name\":\"Infinispan Client\",\"message\":\"Hello World, Infinispan is up!\"}")
              .when()
              .post("/greeting/quarkus")
              .then()
              .statusCode(200);

        given()
                .when().get("/greeting/quarkus")
                .then()
                .statusCode(200)
                .body(is("{\"name\":\"Infinispan Client\",\"message\":\"Hello World, Infinispan is up!\"}"));
    }
}
