package org.acme.s3;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.Response.Status;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@QuarkusTest
@QuarkusTestResource(S3Resource.class)
public class S3ResourcesTest {

    private static final String FILE_NAME_PREFIX = "test-file-";
    private static final String FILE_MIMETYPE = "text/plain";

    @ParameterizedTest
    @ValueSource(strings = {"s3", "async-s3"})
    void testResource(final String testedResource) {
        List<String> data = Arrays.asList("Cherry", "Pear");

        //Upload files
        data.forEach(fruit -> {
                given()
                    .pathParam("resource", testedResource)
                    .multiPart("file", fruit)
                    .multiPart("filename", FILE_NAME_PREFIX + fruit)
                    .multiPart("mimetype", FILE_MIMETYPE)
                    .when()
                    .post("/{resource}/upload")
                    .then()
                    .statusCode(Status.CREATED.getStatusCode());
            }
        );

        //List files
        given()
            .pathParam("resource", testedResource)
            .when().get("/{resource}")
            .then()
            .statusCode(200)
            //Objects are sorted by modified data, so the last added will be the first from list files
            .body("size()", equalTo(2))
            .body("[0].objectKey", equalTo(FILE_NAME_PREFIX + data.get(1)))
            .body("[0].size", equalTo(data.get(1).length()))
            .body("[1].objectKey", equalTo(FILE_NAME_PREFIX + data.get(0)))
            .body("[1].size", equalTo(data.get(0).length()));

        //Download file
        data.forEach(fruit ->
            given()
                .pathParam("resource", testedResource)
                .pathParam("objectKey", FILE_NAME_PREFIX + fruit)
                .when().get("/{resource}/download/{objectKey}")
                .then()
                .statusCode(200)
                .body(equalTo(fruit))
        );
    }
}
