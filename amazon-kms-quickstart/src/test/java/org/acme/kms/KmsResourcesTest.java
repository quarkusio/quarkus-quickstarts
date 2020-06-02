package org.acme.kms;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@QuarkusTest
@QuarkusTestResource(KmsResource.class)
public class KmsResourcesTest {

    private final static String TEST_TEXT = "Quarkus is awsome";

    @ParameterizedTest
    @ValueSource(strings = {"sync", "async"})
    void testResource(final String testedResource) {
        //Encrypt text
        String encryptedText = given()
            .pathParam("resource", testedResource)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
            .body(TEST_TEXT)
            .when()
            .post("/{resource}/encrypt")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .asString();

        assertThat(encryptedText, notNullValue());

        //Decrypt
        given()
            .pathParam("resource", testedResource)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
            .body(encryptedText)
            .when()
            .post("/{resource}/decrypt")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .body(containsString(TEST_TEXT));
    }
}
