package org.acme.quarkus.sample;

import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SourceOfData {

    @Outgoing("prices")
    public PublisherBuilder<Integer> source() {
        return ReactiveStreams.of(1, 2, 3);
    }
}
