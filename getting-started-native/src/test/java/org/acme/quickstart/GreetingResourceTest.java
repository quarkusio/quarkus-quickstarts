package org.acme.quickstart;

import org.jboss.shamrock.test.ShamrockTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@RunWith(ShamrockTest.class)    
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("app/hello")
          .then()
             .statusCode(200)  
             .body(is("hello"));
    }

    @Test
    public void testGreetingEndpoint() {
        String uuid = UUID.randomUUID().toString();
        given()
          .pathParam("name", uuid)
          .when().get("app/hello/greeting/{name}")
          .then()
            .statusCode(200)
            .body(is("hello " + uuid));
    }

}