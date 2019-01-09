package org.acme.restclient;

import org.jboss.shamrock.test.ShamrockTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(ShamrockTest.class)    
public class CountriesResourceTest {

    @Test
    public void testNameEndpoint() {
        given()
          .when().get("app/country/name/gree")
          .then()
             .statusCode(200)  
             .body("size()", equalTo(2));
    }

}