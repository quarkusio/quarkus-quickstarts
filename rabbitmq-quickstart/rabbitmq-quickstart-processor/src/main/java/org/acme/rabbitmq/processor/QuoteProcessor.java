package org.acme.rabbitmq.processor;

import java.util.Random;

import javax.enterprise.context.ApplicationScoped;

import org.acme.rabbitmq.model.Quote;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.reactive.messaging.annotations.Blocking;

/**
 * A bean consuming data from the "request" RabbitMQ queue and giving out a random quote.
 * The result is pushed to the "quotes" RabbitMQ exchange.
 */
@ApplicationScoped
public class QuoteProcessor {

    private Random random = new Random();

    @Incoming("requests")
    @Outgoing("quotes")
    @Blocking
    public Quote process(String quoteRequest) throws InterruptedException {
        // simulate some hard working task
        Thread.sleep(200);
        return new Quote(quoteRequest, random.nextInt(100));
    }
}
