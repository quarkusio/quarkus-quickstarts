package org.acme.infinispan.client;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

public class CacheResource implements QuarkusTestResourceLifecycleManager {

    private static GenericContainer INFINISPAN = null;
    private static final Integer INFINISPAN_PORT = 11222;

    private static RemoteCacheManager cacheManager;

    @Override
    public Map<String, String> start() {

        INFINISPAN =
                new GenericContainer("infinispan/server:11.0.4.Final")
                        .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Infinispan Server.*started in.*\\s"))
                        .withStartupTimeout(Duration.ofMillis(20000))
                .withEnv("USER","Titus Bramble")
                .withEnv("PASS","Shambles");

        INFINISPAN.start();
        final String hosts = INFINISPAN.getContainerIpAddress() + ":" + INFINISPAN.getMappedPort(INFINISPAN_PORT);

        return Collections.singletonMap("quarkus.infinispan-client.server-list", hosts);
    }

    @Override
    public void stop() {
        INFINISPAN.stop();
    }
}
