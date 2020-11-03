package org.acme.hibernate.orm;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

@QuarkusTest
public class FruitsEndpointTest {

    //    Removed because tests are not configured to load data per test.
//    @Test
    public void testListAllFruits() {
        //List all, should have all 3 fruits the database has initially:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        containsString("Cherry"),
                        containsString("Apple"),
                        containsString("Banana"));

        //Delete the Cherry:
        given()
                .when().delete("/fruits/1")
                .then()
                .statusCode(204);

        //List all, cherry should be missing now:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        not(containsString("Cherry")),
                        containsString("Apple"),
                        containsString("Banana"));

        //Create the Pear:
        given()
                .when()
                .body("{\"name\" : \"Pear\"}")
                .contentType("application/json")
                .post("/fruits")
                .then()
                .statusCode(201);

        //List all, cherry should be missing now:
        given()
                .when().get("/fruits")
                .then()
                .statusCode(200)
                .body(
                        not(containsString("Cherry")),
                        containsString("Apple"),
                        containsString("Banana"),
                        containsString("Pear"));
    }

    @Test
    public void testListFirstFruit() {

        PrintStream out = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        given()
                .when().get("/fruits/1")
                .then()
                .statusCode(200);

        String sysOut = baos.toString();
        System.setOut(out);

        System.out.println(sysOut);

        // Setting quarkus.hibernate-orm.fetch.batch-size property is what breaks this.
        Assert.assertTrue("SQL must contain 'left outer join' per the NamedEntityGraph load query hint",
                sysOut.contains("left outer join"));
    }

}
