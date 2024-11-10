package org.acme.ses;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.any;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@QuarkusTest
public class SesResourcesTest {

    private static final String JSON = "{\"from\":\"%s\", \"to\":\"%s\", \"subject\":\"%s\", \"body\":\"%s\"}";

    private final static String FROM_EMAIL = "from-quarkus@example.com";
    private final static String TO_EMAIL = "to-quarkus@example.com";

    @ParameterizedTest
    @ValueSource(strings = {"sync", "async"})
    void testResource(final String testedResource) {

        //Send email
        given()
            .pathParam("resource", testedResource)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .body(String.format(JSON, FROM_EMAIL, TO_EMAIL, "Hello from Quarkus", "Quarkus is awsome"))
            .when()
            .post("/{resource}/email")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .body(any(String.class));
    }
}
