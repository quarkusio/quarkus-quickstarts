package org.acme.rest.client;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;

import org.acme.rest.client.resources.WireMockExtensions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class ExtensionsResourceTest {

    @Test
    public void testExtensionsIdEndpoint() {
        given()
            .when().get("/extension/id/io.quarkus:quarkus-rest-client")
            .then()
            .statusCode(200)
            .body("$.size()", is(1),
                "[0].id", is("io.quarkus:quarkus-rest-client"),
                "[0].name", is("REST Client"),
                "[0].keywords.size()", greaterThan(1),
                "[0].keywords", hasItem("rest-client"));
    }

    @Test
    public void testExtensionIdAsyncEndpoint() {
        given()
            .when().get("/extension/id-async/io.quarkus:quarkus-rest-client")
            .then()
            .statusCode(200)
            .body("$.size()", is(1),
                "[0].id", is("io.quarkus:quarkus-rest-client"),
                "[0].name", is("REST Client"),
                "[0].keywords.size()", greaterThan(1),
                "[0].keywords", hasItem("rest-client"));
    }

    @Test
    public void testExtensionIdMutinyEndpoint() {
        given()
            .when().get("/extension/id-uni/io.quarkus:quarkus-rest-client")
            .then()
            .statusCode(200)
            .body("$.size()", is(1),
                "[0].id", is("io.quarkus:quarkus-rest-client"),
                "[0].name", is("REST Client"),
                "[0].keywords.size()", greaterThan(1),
                "[0].keywords", hasItem("rest-client"));
    }
}
