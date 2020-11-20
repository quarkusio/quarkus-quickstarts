package org.acme.redis.streams.consumer.processors;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;

import java.util.Collections;
import java.util.Map;

import static java.lang.String.format;

public class RedisResource implements QuarkusTestResourceLifecycleManager {

    public static final GenericContainer REDIS = new GenericContainer("redis:5.0.6").withExposedPorts(6379);

    @Override
    public Map<String, String> start() {
        REDIS.start();
        return Collections.singletonMap("quarkus.redis.hosts", format("redis://%s:%d", REDIS.getContainerIpAddress(), REDIS.getFirstMappedPort()));
    }

    @Override
    public void stop() {
        REDIS.stop();
    }
}
