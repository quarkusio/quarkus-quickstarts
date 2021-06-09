package org.acme.kafka;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.awaitility.Awaitility.await;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class PriceResourceTest {

    private static final String PRICES_SSE_ENDPOINT = "http://localhost:8081/prices/stream";

    @Test
    void testPricesEventStream() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(PRICES_SSE_ENDPOINT);

        List<String> received = new CopyOnWriteArrayList<>();

        SseEventSource source = SseEventSource.target(target).build();
        source.register(inboundSseEvent -> received.add(inboundSseEvent.readData(String.class)));
        source.open();
        await().atMost(100000, MILLISECONDS).until(() -> received.size() == 3);
        source.close();
    }
}