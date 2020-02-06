package org.acme.mqtt;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.sse.SseEventSource;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class PriceResourceTest {

    @TestHTTPResource("prices/stream")
    URI pricesUrl;

    @Test
    public void shouldGetHello() {
        given()
                .when().get("/prices")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }

    @Test
    public void shouldGetStreamOfPrices() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(pricesUrl);

        AtomicInteger priceCount = new AtomicInteger();

        try (SseEventSource source = SseEventSource.target(target).build()) {
            source.register(event -> {
                Double value = event.readData(Double.class);
                System.out.println("Received price: " + value);
                priceCount.incrementAndGet();
            });
            source.open();
            Thread.sleep(15 * 1000L);
        } catch (InterruptedException ignored) {
        }

        int count = priceCount.get();
        assertTrue(count > 1, "Expected more than 2 prices read from the source, got " + count);
    }

}
