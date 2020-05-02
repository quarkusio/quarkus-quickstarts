package org.acme.tokenmanager.controllers;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.acme.tokenmanager.controllers.dtos.Entry;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

@QuarkusTest
public class CacheResourceCreateIntegrationTest {

    public static final String TAX_CODE = "CBTPGA90B13B345H";
    public static final String TAX_CODE_WRONG = "CBTPGA90B13B34";

    @Test
    public void test_add_entry() {

        RestAssured
                .given()
                    .body(new Entry(TAX_CODE))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                .when()
                    .post("/tokens")
                .then()
                    .statusCode(Status.CREATED.getStatusCode())
                    .body(notNullValue())
                    .body("token", not(emptyString()))
                    .body("uri", not(emptyString()))
                    .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void test_add_entry_as_xml() {

        RestAssured
                .given()
                    .body(new Entry(TAX_CODE))
                    .contentType(MediaType.APPLICATION_XML)
                .when()
                    .post("/tokens")
                .then()
                    .statusCode(Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode());
    }


    @Test
    public void test_add_entry_wrong_tax_code() {

        RestAssured
                .given()
                    .body(new Entry(TAX_CODE_WRONG))
                    .contentType(MediaType.APPLICATION_JSON)
                .when()
                    .post("/tokens")
                .then()
                    .statusCode(Status.BAD_REQUEST.getStatusCode());
    }
}