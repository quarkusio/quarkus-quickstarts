package org.acme.quarkus.sample;

import io.reactivex.Flowable;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * A bean producing random prices every 5 seconds.
 * The prices are written to a MQTT topic (prices). The MQTT configuration is specified in the application configuration.
 */
@ApplicationScoped
public class PriceGenerator {

    private Random random = new Random();

    @Outgoing("topic-price")
    public Flowable<Integer> generate() {
        return Flowable.interval(5, TimeUnit.SECONDS)
                .map(tick -> {
                    int price = random.nextInt(100);
                    System.out.println("Sending price: " + price);
                    return price;
                });
    }

}
