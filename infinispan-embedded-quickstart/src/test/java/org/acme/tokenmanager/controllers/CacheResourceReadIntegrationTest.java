package org.acme.tokenmanager.controllers;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.acme.tokenmanager.controllers.dtos.Entry;
import org.acme.tokenmanager.services.CacheService;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import static org.hamcrest.CoreMatchers.equalToObject;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class CacheResourceReadIntegrationTest {

    public static final String TOKEN_PATH_PARAM = "token";
    public static final String TAX_CODE = "CBTPGA90B13B345H";

    @Inject
    private CacheService cacheService;

    @Test
    public void test_get_not_existing_entry() {
        RestAssured
                .given()
                    .pathParam(TOKEN_PATH_PARAM, "xyz-xyz")
                .when()
                    .get("/tokens/{token}")
                .then()
                    .statusCode(200)
                    .body(notNullValue());
    }

    @Test
    public void test_get_existing_entry() {

        //initialize cache since it is empty at the beginning
        Entry entry = new Entry(TAX_CODE);
        String token = cacheService.putInCache(entry);

        Entry response =
                RestAssured
                .given()
                    .pathParam(TOKEN_PATH_PARAM, token)
                    .accept(MediaType.APPLICATION_JSON)
                .when()
                    .get("/tokens/{token}")
                .then()
                    .statusCode(200)
                    .body(notNullValue())
                    .extract().body().as(Entry.class);

        assertThat(response, equalToObject(entry));

    }

    @Test
    public void test_get_entry_different_consumed_media_type() {

        RestAssured
                .given()
                    .pathParam(TOKEN_PATH_PARAM, "xyz-xyz")
                    .accept(MediaType.APPLICATION_XML)
                .when()
                    .get("/tokens/{token}")
                .then()
                    .statusCode(Status.NOT_ACCEPTABLE.getStatusCode());
    }

    @Test
    public void test_get_entry_null_token() {

        assertThrows(IllegalArgumentException.class, () ->

            RestAssured
                    .given()
                        .pathParam(TOKEN_PATH_PARAM, null)
                    .when()
                        .get("/tokens/{token}")
        );
    }
}