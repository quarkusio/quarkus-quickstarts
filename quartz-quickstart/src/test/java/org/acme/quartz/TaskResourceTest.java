package org.acme.quartz;

import io.quarkus.test.junit.QuarkusTest;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class TaskResourceTest {

    @Test
    public void tasks() throws InterruptedException {
        Thread.sleep(1000); // wait at least a second to have the first task created
        given()
                .when().get("/tasks")
                .then()
                .statusCode(200)
                .body("size()", is(greaterThanOrEqualTo(1)));
    }

}
