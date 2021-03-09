package org.acme.mongodb;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@QuarkusTestResource(MongoResource.class)
public class FruitResourceTest {

    private static final BiFunction<String, String, String> FRUIT = (name, description) -> String
            .format("{\"name\":\"%s\", \"description\":\"%s\"}", name, description);

    @ParameterizedTest
    @ValueSource(strings = {"fruits", "reactive_fruits", "codec_fruits"})
    void testListAllFruits(final String testedResource) {

        List<String> data = Arrays.asList("Lime", "Coconut");

        data.forEach(fruit -> {
            given()
                    .pathParam("resource", testedResource)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .body(FRUIT.apply(fruit, "description-" + fruit))
                    .when()
                    .post("/{resource}")
                    .then()
                    .statusCode(Response.Status.OK.getStatusCode());
        });


        given()
                .pathParam("resource", testedResource)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .when()
                .get("/{resource}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("[0].name", equalTo(data.get(0)))
                .body("[0].description", equalTo("description-" + data.get(0)))
                .body("[1].name", equalTo(data.get(1)))
                .body("[1].description", equalTo("description-" + data.get(1)));
    }
}
