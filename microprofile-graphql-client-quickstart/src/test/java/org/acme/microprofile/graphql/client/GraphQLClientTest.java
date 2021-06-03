package org.acme.microprofile.graphql.client;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.get;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class GraphQLClientTest {

    @Test
    public void testTypesafeClient() {
        List<String> filmTitles = get("/typesafe")
                .then()
                .extract().response().path("title");
        assertTrue(filmTitles.contains("The Empire Strikes Back"));
    }

    @Test
    public void testDynamicClient() {
        List<String> filmTitles = get("/dynamic")
                .then()
                .extract().response().path("title");
        assertTrue(filmTitles.contains("The Empire Strikes Back"));
    }

}
