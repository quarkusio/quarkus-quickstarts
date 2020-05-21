package org.acme.sns;

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
@QuarkusTestResource(SnsResource.class)
public class SnsResourcesTest {

    @ParameterizedTest
    @ValueSource(strings = {"sync", "async"})
    void testPublisher(final String testedResource) {
        given()
            .pathParam("resource", testedResource)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .body(String
                .format("{\"flavor\":\"%s\", \"spin\":\"%s\"}", "Charm", "1/2"))
            .when()
            .post("/{resource}/cannon/shoot")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .body(any(String.class));
    }

//    @ParameterizedTest
//    @ValueSource(strings = {"sync", "async"})
//    void testSubscriber(final String testedResource) {
//        given()
//            .pathParam("resource", testedResource)
//            .when()
//            .post("/{resource}/shield/subscribe")
//            .then()
//            .statusCode(Status.OK.getStatusCode())
//            .body(any(String.class));
//    }
}
