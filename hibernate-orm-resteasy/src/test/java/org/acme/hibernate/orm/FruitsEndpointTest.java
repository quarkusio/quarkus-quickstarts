package org.acme.hibernate.orm;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class FruitsEndpointTest {

    @Test
    public void testListAllFruits() {
        //List all, should have all 3 fruits the database has initially:
        given()
              .when().get("/fruits")
              .then()
              .statusCode(200)
              .body(
                    containsString("Cherry"),
                    containsString("Apple"),
                    containsString("Banana")
                    );

        //Delete the Cherry:
        given()
              .when().delete("/fruits/1")
              .then()
              .statusCode(204)
              ;

        //List all, cherry should be missing now:
        given()
              .when().get("/fruits")
              .then()
              .statusCode(200)
              .body(
                    not( containsString("Cherry") ),
                    containsString("Apple"),
                    containsString("Banana")
              );
    }

}
