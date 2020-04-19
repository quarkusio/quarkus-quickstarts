package org.acme.dynamodb;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@QuarkusTest
@QuarkusTestResource(DynamodbResource.class)
public class DynamodbResourcesTest {

    private static final BiFunction<String, String, String> FRUIT = (name, description) -> String
        .format("{\"name\":\"%s\", \"description\":\"%s\"}", name, description);

    @ParameterizedTest
    @ValueSource(strings = {"fruits", "async-fruits"})
    void testResource(final String testedResource) {
        List<String> data = Arrays.asList("Cherry", "Pear");

        //Add fruits
        data.forEach(fruit -> {
                given()
                    .pathParam("resource", testedResource)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(FRUIT.apply(fruit, "description-" + fruit))
                    .when()
                    .post("/{resource}")
                    .then()
                    .statusCode(Status.OK.getStatusCode());
            }
        );

        //List fruits
        given()
            .pathParam("resource", testedResource)
            .when().get("/{resource}")
            .then()
            .statusCode(200)
            .body("size()", equalTo(2))
            .body("[0].name", equalTo(data.get(1)))
            .body("[0].description", equalTo("description-" + data.get(1)))
            .body("[1].name", equalTo(data.get(0)))
            .body("[1].description", equalTo("description-" + data.get(0)));

        //Get single fruit
        data.forEach(fruit ->
            given()
                .pathParam("resource", testedResource)
                .pathParam("key", fruit)
                .when().get("/{resource}/{key}")
                .then()
                .statusCode(200)
                .body("name", equalTo(fruit))
                .body("description", equalTo("description-" + fruit))
        );
    }
}
