package org.kie.pmml.kogito.quarkus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class LogisticRegressionIrisDataTest {

    private static final String BASE_PATH = "/LogisticRegressionIrisData";
    private static final String DESCRIPTIVE_PATH = BASE_PATH + "/descriptive";
    private static final String TARGET = "Species";
    private static final String INPUT_DATA = "{\n" +
            "  \"Sepal.Length\": 6.9,\n" +
            "  \"Sepal.Width\": 3.1,\n" +
            "  \"Petal.Length\": 5.1,\n" +
            "  \"Petal.Width\": 2.3\n" +
            "}";

    @Test
    void testEvaluateLogisticRegressionIrisDataResult() {
        given()
                .contentType(ContentType.JSON)
                .body(INPUT_DATA)
                .when()
                .post(BASE_PATH)
                .then()
                .statusCode(200)
                .body(TARGET, is("virginica"));
    }

    @Test
    void testEvaluateLogisticRegressionIrisDataDescriptive() {
        final Map<String, Object> expectedResultMap = Collections.singletonMap(TARGET, "virginica");
        Object resultVariables = given()
                .contentType(ContentType.JSON)
                .body(INPUT_DATA)
                .when()
                .post(DESCRIPTIVE_PATH)
                .then()
                .statusCode(200)
                .body("correlationId", is(nullValue()))
                .body("segmentationId", is(nullValue()))
                .body("segmentId", is(nullValue()))
                .body("segmentIndex", is(0)) // as JSON is not schema aware, here we assert the RAW string
                .body("resultCode", is("OK"))
                .body("resultObjectName", is(TARGET))
                .extract()
                .path("resultVariables");
        assertNotNull(resultVariables);
        assertTrue(resultVariables instanceof Map);
        Map<String, Object> mappedResultVariables = (Map) resultVariables;
        expectedResultMap.forEach((key, value) -> {
            assertTrue(mappedResultVariables.containsKey(key));
            assertEquals(value, mappedResultVariables.get(key));
        });
    }

}
