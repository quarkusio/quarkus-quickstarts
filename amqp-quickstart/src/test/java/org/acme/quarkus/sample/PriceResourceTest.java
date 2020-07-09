package org.acme.quarkus.sample;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@Testcontainers
@QuarkusTest
class PriceResourceTest {

    private static final String PRICES_SSE_ENDPOINT = "http://localhost:8081/prices/stream";

    @Container
    public static final GenericContainer BROKER = new FixedHostPortGenericContainer(
            "vromero/activemq-artemis:2.10.1-alpine")
            .withFixedExposedPort(5672, 5672)
            .withFixedExposedPort(8161, 8161)
            .withEnv("ARTEMIS_USERNAME", "quarkus")
            .withEnv("ARTEMIS_PASSWORD", "quarkus")
            .waitingFor(Wait.forHttp("/").forPort(8161));

    @Test
    void testPricesEventStream() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(PRICES_SSE_ENDPOINT);

        List<Double> received = new CopyOnWriteArrayList<>();

        SseEventSource source = SseEventSource.target(target).build();
        source.register(inboundSseEvent -> {
            received.add(Double.valueOf(inboundSseEvent.readData()));
        });
        source.open();
        await().atMost(60, SECONDS).until(() -> received.size() == 2);
        source.close();
    }
}