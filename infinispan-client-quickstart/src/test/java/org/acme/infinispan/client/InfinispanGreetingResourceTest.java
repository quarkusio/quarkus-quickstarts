package org.acme.infinispan.client;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

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

       Greeting greeting = given()
                .when().get("/greeting/quarkus")
                .then()
                .statusCode(200)
                .extract()
                .as(Greeting.class);
       Assertions.assertThat(greeting.name()).isEqualTo("Infinispan Client");
       Assertions.assertThat(greeting.message()).isEqualTo("Hello World, Infinispan is up!");
    }
}
