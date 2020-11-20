package org.acme.redis.streams.consumer.config;

import io.quarkus.redis.client.RedisClient;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.acme.redis.streams.consumer.util.AppConstants.CONSUMER_GROUP;
import static org.acme.redis.streams.consumer.util.AppConstants.TEMPERATURE_VALUES_STREAM;
import static org.acme.redis.streams.consumer.util.RedisSupport.toXGroupCommand;

public class StreamConfig {

    private static final Logger log = LoggerFactory.getLogger(StreamConfig.class);

    @Inject
    RedisClient client;

    public void init(@Observes @Priority(1) StartupEvent event) {
        List<String> commands = toXGroupCommand(TEMPERATURE_VALUES_STREAM, CONSUMER_GROUP);

        try {
            log.info("Creating consumer-group...");
            this.client.xgroup(commands);
            log.info("Created consumer-group '{}'", CONSUMER_GROUP);
        } catch (Exception e) {
            // if the consumer starts before the producer, then the stream might not exist (at the very first time).
            // so we create the stream & the consumer-group with one command here.
            if (e.getMessage().contains("MKSTREAM")) {
                var command = new ArrayList<>(commands);
                command.add("MKSTREAM");
                this.client.xgroup(command);
                log.info("Created stream '{}' and consumer-group '{}'", TEMPERATURE_VALUES_STREAM, CONSUMER_GROUP);
            } else {
                log.info("Consumer-group already exists, skipping it");
            }
        }
    }
}
