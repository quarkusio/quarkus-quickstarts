package org.acme.ssm;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(SsmResource.class)
public class SsmResourcesTest {

    @ParameterizedTest
    @ValueSource(strings = { "sync", "async" })
    void testResource(final String testedResource) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("secure", "stored as cipher text");
        data.put("plain", "stored as plain text");

        //Add params
        data.forEach((name, value) -> given()
                .pathParam("resource", testedResource)
                .pathParam("name", name)
                .queryParam("secure", "secure".equals(name))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
                .body(value)
                .when()
                .put("/{resource}/{name}")
                .then()
                .statusCode(Status.NO_CONTENT.getStatusCode()));

        //List params
        given()
                .pathParam("resource", testedResource)
                .when()
                .get("/{resource}")
                .then()
                .statusCode(200)
                .body("secure", equalTo(data.get("secure")))
                .body("plain", equalTo(data.get("plain")));

        //Get each param
        data.forEach((name, value) -> given()
                .pathParam("resource", testedResource)
                .pathParam("name", name)
                .when().get("/{resource}/{name}")
                .then()
                .statusCode(200)
                .body(equalTo(value)));
    }
}
