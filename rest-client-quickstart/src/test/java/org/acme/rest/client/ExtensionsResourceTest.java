package org.acme.rest.client;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import org.acme.rest.client.resources.WireMockExtensions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class ExtensionsResourceTest {

    @Test
    public void testExtensionsIdEndpoint() {
        given()
            .when().get("/extension/id/io.quarkus:quarkus-rest-client-reactive")
            .then()
            .statusCode(200)
            .body("$.size()", is(1),
                "[0].id", is("io.quarkus:quarkus-rest-client-reactive"),
                "[0].name", is("REST Client Reactive"),
                "[0].keywords.size()", greaterThan(1),
                "[0].keywords", hasItem("rest-client"));
    }

    @Test
    public void testExtensionIdAsyncEndpoint() {
        given()
            .when().get("/extension/id-async/io.quarkus:quarkus-rest-client-reactive")
            .then()
            .statusCode(200)
            .body("$.size()", is(1),
                "[0].id", is("io.quarkus:quarkus-rest-client-reactive"),
                "[0].name", is("REST Client Reactive"),
                "[0].keywords.size()", greaterThan(1),
                "[0].keywords", hasItem("rest-client"));
    }

    @Test
    public void testExtensionIdMutinyEndpoint() {
        given()
            .when().get("/extension/id-uni/io.quarkus:quarkus-rest-client-reactive")
            .then()
            .statusCode(200)
            .body("$.size()", is(1),
                "[0].id", is("io.quarkus:quarkus-rest-client-reactive"),
                "[0].name", is("REST Client Reactive"),
                "[0].keywords.size()", greaterThan(1),
                "[0].keywords", hasItem("rest-client"));
    }
}
