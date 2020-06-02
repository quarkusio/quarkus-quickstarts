package org.acme.ses;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.any;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@QuarkusTest
@QuarkusTestResource(SesResource.class)
public class SesResourcesTest {

    private static final String JSON = "{\"from\":\"%s\", \"to\":\"%s\", \"subject\":\"%s\", \"body\":\"%s\"}";

    @ParameterizedTest
    @ValueSource(strings = {"sync", "async"})
    void testResource(final String testedResource) {

        //Send email
        given()
            .pathParam("resource", testedResource)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .body(String.format(JSON, SesResource.FROM_EMAIL, SesResource.TO_EMAIL, "Hello from Quarkus", "Quarkus is awsome"))
            .when()
            .post("/{resource}/email")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .body(any(String.class));
    }
}
