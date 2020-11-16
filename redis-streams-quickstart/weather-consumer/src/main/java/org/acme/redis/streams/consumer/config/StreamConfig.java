package org.acme.redis.streams.consumer.config;

import io.quarkus.redis.client.RedisClient;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import static org.acme.redis.streams.consumer.util.AppConstants.CONSUMER_GROUP;
import static org.acme.redis.streams.consumer.util.AppConstants.TEMPERATURE_VALUES_STREAM;
import static org.acme.redis.streams.consumer.util.RedisSupport.toXGroupCommand;

public class StreamConfig {

    static final Logger log = LoggerFactory.getLogger(StreamConfig.class);

    @Inject
    RedisClient client;

    public void init(@Observes @Priority(1) StartupEvent event) {
        try {
            this.client.xgroup(toXGroupCommand(TEMPERATURE_VALUES_STREAM, CONSUMER_GROUP));
            log.info("Created consumer-group: {}", CONSUMER_GROUP);
        } catch (Exception e) {
            log.debug("Consumer-group already exists, skipping it.");
        }
    }
}
