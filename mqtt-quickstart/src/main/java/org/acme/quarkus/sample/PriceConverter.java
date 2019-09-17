package org.acme.quarkus.sample;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;

/**
 * A bean consuming data from the "prices" MQTT topic and applying some conversion.
 * The result is pushed to the "my-data-stream" stream which is an in-memory stream.
 */
@ApplicationScoped
public class PriceConverter {

    private static final double CONVERSION_RATE = 0.88;

    @Incoming("prices")
    @Outgoing("my-data-stream")
    @Broadcast
    public double process(byte[] priceRaw) {
        int priceInUsd = Integer.valueOf(new String(priceRaw));
        System.out.println("Receiving price: " + priceInUsd);
        return priceInUsd * CONVERSION_RATE;
    }

}
