package org.acme;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

@QuarkusTest
@QuarkusTestResource(ConsulTestResource.class)
public class FrontendApiTest {

    @Test
    public void test() {
        Set<String> bodies = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            bodies.add(RestAssured.get("/api").then().statusCode(200).extract().body().asString());
        }
        Assertions.assertEquals(2, bodies.size());
    }


}
