package org.acme.quarkus.sample;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class PriceResourceTest {

    private static final String PRICES_SSE_ENDPOINT = "http://localhost:8081/prices/stream";

    @Test
    void testPricesEventStream() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(PRICES_SSE_ENDPOINT);

        List<Double> received = new ArrayList<>();

        try (SseEventSource source = SseEventSource.target(target).build()) {
            source.register(inboundSseEvent -> received.add(Double.valueOf(inboundSseEvent.readData())));
            source.open();
            Thread.sleep(500); // Consume events for just 500 ms
        } catch (InterruptedException e) {
            System.out.println("Something went wrong!");

        }

        assertEquals(Arrays.asList(0.88, 1.76, 2.64), received);
    }
}