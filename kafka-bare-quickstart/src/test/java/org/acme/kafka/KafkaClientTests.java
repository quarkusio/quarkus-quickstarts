package org.acme.kafka;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.core.StringContains.containsString;

@QuarkusTest
@QuarkusTestResource(KafkaResource.class)
class KafkaClientTests {

    @Test
    void testBareClients() {
        given()
                .queryParam("key", "my-key")
                .queryParam("value", "my-value")
                .when()
                .post("/kafka")
                .then()
                .statusCode(200);

        await().untilAsserted(() ->
                get("/kafka")
                        .then()
                        .statusCode(200)
                        .body(containsString("my-key-my-value"))
        );

        get("/kafka/topics")
                .then()
                .statusCode(200)
                .body(containsString("hello"));
    }

    @Test
    void testVertxClients() {
        given()
                .queryParam("key", "my-key-vertx")
                .queryParam("value", "my-value-vertx")
                .when()
                .post("/vertx-kafka")
                .then()
                .statusCode(200);

        await().untilAsserted(() ->
                get("/vertx-kafka")
                        .then()
                        .statusCode(200)
                        .body(containsString("my-key-vertx-my-value-vertx"))
        );

        get("/vertx-kafka/topics")
                .then()
                .statusCode(200)
                .body(containsString("hello-vertx"));
    }
}