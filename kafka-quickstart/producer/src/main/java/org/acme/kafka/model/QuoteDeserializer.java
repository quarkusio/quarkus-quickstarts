package org.acme.kafka.model;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class QuoteDeserializer extends ObjectMapperDeserializer<Quote> {
    public QuoteDeserializer() {
        // pass the class to the parent.
        super(Quote.class);
    }
}
