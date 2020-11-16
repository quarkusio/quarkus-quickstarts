package org.acme.redis.streams.consumer.listeners;


import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import org.acme.redis.streams.consumer.processors.TemperatureProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

public class TemperatureStreamListener {

    static final Logger log = LoggerFactory.getLogger(TemperatureStreamListener.class);

    @Inject
    TemperatureProcessor processor;

    public void init(@Observes @Priority(2) StartupEvent event) {
        log.info("Starting processing temperatures");

        Multi.createBy().repeating()
                .supplier(() -> this.processor.calculateAggregates())
                .indefinitely()
                .onItem().disjoint()
                .onOverflow().drop(res -> log.warn("Dropping msg: {}", res))
                .subscribe().with(res -> log.debug("Processed msg({})", res), err -> log.error("Caught exception: {}", err));
    }
}
