package org.acme.s3;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import javax.ws.rs.core.Response.Status;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@QuarkusTest
@QuarkusTestResource(S3Resource.class)
public class S3ResourcesTest {

    private static final String FILE_NAME_PREFIX = "test-file-";
    private static final String FILE_MIMETYPE = "text/plain";

    private static final Map<String, File> FRUITS = Map.of(
            "cherry", testFile("cherry"),
            "pear", testFile("pear"));

    private static File testFile(String name) {
        return new File("./src/test/resources/" + name);
    }

    @ParameterizedTest
    @ValueSource(strings = {"s3", "async-s3"})
    void testResource(final String testedResource) {

        //Upload files
        FRUITS.forEach((name, file) -> {
                    given()
                            .pathParam("resource", testedResource)
                            .multiPart("file", file)
                            .multiPart("filename", FILE_NAME_PREFIX + name)
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
                //Objects are sorted by objectKey
                .body("size()", equalTo(2))
                .body("[0].objectKey", equalTo(FILE_NAME_PREFIX + "cherry"))
                .body("[0].size", equalTo(fruitFileLength("cherry")))
                .body("[1].objectKey", equalTo(FILE_NAME_PREFIX + "pear"))
                .body("[1].size", equalTo(fruitFileLength("pear")));

        //Download file
        FRUITS.forEach((name, file) -> {

            try {
                given()
                        .pathParam("resource", testedResource)
                        .pathParam("objectKey", FILE_NAME_PREFIX + name)
                        .when().get("/{resource}/download/{objectKey}")
                        .then()
                        .statusCode(200)
                        .body(equalTo(fruitFileData(name))
                        );
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    private String fruitFileData(String name) throws IOException {
        return Files.readString(FRUITS.get(name).toPath());
    }

    private int fruitFileLength(String name) {
        return (int) FRUITS.get(name).length();
    }
}
