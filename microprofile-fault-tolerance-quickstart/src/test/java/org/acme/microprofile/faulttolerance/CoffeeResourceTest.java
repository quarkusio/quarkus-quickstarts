package org.acme.microprofile.faulttolerance;

import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CoffeeResourceTest extends BaseTest {

    @Inject
    private CoffeeResource coffeeResource;

    @Test
    public void testCoffeeList() {
        coffeeResource.resetCounter();
        coffeeResource.setFailRatio(0f);
        get("/coffee")
                .then()
                .statusCode(200)
                .body("id", hasItems(1, 2, 3))
                .body("countryOfOrigin", hasItems("Colombia", "Bolivia", "Vietnam"));
        Assertions.assertEquals(1, coffeeResource.getCounter().longValue());

        coffeeResource.resetCounter();
        coffeeResource.setFailRatio(1f);
        get("/coffee")
                .then()
                .statusCode(500);
        Assertions.assertEquals(5, coffeeResource.getCounter().longValue());
    }

    @Test
    public void testCoffeeDetail() {
        coffeeResource.setFailRatio(0f);
        get("/coffee/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("countryOfOrigin", is("Colombia"));

        coffeeResource.setFailRatio(1f);
        get("/coffee/1")
                .then()
                .statusCode(500);
    }
}
