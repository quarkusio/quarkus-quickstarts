package org.acme;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.WebSocket;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.fail;

@QuarkusTest
public class CostAppTest {

    @TestHTTPResource("/costs")
    URI testUri;

    @Test
    public void testCostPassingThrough() {
        Vertx testVertx = Vertx.vertx();

        testVertx.createHttpClient().webSocket(testUri.getPort(), testUri.getHost(), testUri.getPath())
                .onComplete(
                        result -> {
                            if (result.succeeded()) {
                                WebSocket webSocket = result.result();
                                webSocket.writeBinaryMessage(Buffer.buffer("{\"value\": 100, \"currency\": \"PLN\"}"));
                                webSocket.writeBinaryMessage(Buffer.buffer("{\"value\": 10, \"currency\": \"CHF\"}"));
                            } else {
                                fail("Failed to establish a WebSocket connection", result.cause());
                            }
                        }
                );

        await("cost added")
                .atMost(10, TimeUnit.SECONDS)
                .until(() -> {
                    String resultAsString = get("/collected-costs").getBody().asString();
                    return Double.valueOf(resultAsString);
                }, comparesEqualTo(31.3));

    }
}