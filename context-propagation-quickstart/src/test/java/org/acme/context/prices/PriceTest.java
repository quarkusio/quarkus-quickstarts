package org.acme.context.prices;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;

import org.acme.context.Price;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;

@QuarkusTest
@QuarkusTestResource(KafkaResource.class)
public class PriceTest {

    private static final String PRICES_SSE_ENDPOINT = "http://localhost:8081/prices";


    @Test
    public void test() {
        // Check we don't have any prices
        List<Price> prices = RestAssured.get("/prices/all").as(new TypeRef<List<Price>>() {});
        Assertions.assertTrue(prices.isEmpty());

        // Stream the prices
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(PRICES_SSE_ENDPOINT);
        List<Double> received = new CopyOnWriteArrayList<>();
        SseEventSource source = SseEventSource.target(target).build();
        source.register(inboundSseEvent -> received.add(Double.valueOf(inboundSseEvent.readData())));
        source.open();

        // Send the prices
        Price p1 = new Price();
        p1.value = 1.0;
        Price p2 = new Price();
        p2.value = 4.0;
        Price p3 = new Price();
        p3.value = 2.0;
        RestAssured.given().header("Content-Type", "application/json").body(p1).post("/").then().statusCode(204);
        RestAssured.given().header("Content-Type", "application/json").body(p2).post("/").then().statusCode(204);
        RestAssured.given().header("Content-Type", "application/json").body(p3).post("/").then().statusCode(204);

        await().atMost(100000, MILLISECONDS).until(() -> received.size() == 3);
        source.close();

        Assertions.assertTrue(received.contains(p1.value));
        Assertions.assertTrue(received.contains(p2.value));
        Assertions.assertTrue(received.contains(p3.value));

        prices = RestAssured.get("/prices/all").as(new TypeRef<List<Price>>() {});
        Assertions.assertEquals(prices.size(), 3);
    }

}
