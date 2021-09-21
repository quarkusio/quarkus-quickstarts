package org.acme.mongodb;

import io.quarkus.test.common.QuarkusTestResource;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@QuarkusTest
@QuarkusTestResource(MongoDbResource.class)
public class FruitResourceTest {

    @ParameterizedTest
    @ValueSource(strings = { "/fruits", "/reactive_fruits", "/codec_fruits" })
    public void testAddAndList(String path) {
        final Fruit fruit1 = new Fruit("fruit1", "fruit description 1");
        final Fruit fruit2 = new Fruit("fruit2", "fruit description 2");

        Fruit[] fruits1 = postFruit(path, fruit1);
        Assertions.assertThat(fruits1).isNotNull();
        Assertions.assertThat(fruits1.length).isEqualTo(1);
        Assertions.assertThat(fruits1[0]).isEqualTo(fruit1);

        fruits1 = postFruit(path, fruit2);
        Assertions.assertThat(fruits1.length).isEqualTo(2);
        Assertions.assertThat(fruits1).contains(fruit1, fruit2);

        Fruit[] fruits2 = RestAssured.get(path)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Fruit[].class);
        Assertions.assertThat(fruits2).isEqualTo(fruits1);
    }

    private Fruit[] postFruit(String path, Fruit fruit2) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(fruit2)
                .post(path)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().body().as(Fruit[].class);
    }
}
