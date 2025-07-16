package org.acme;


import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.Map;

public class ConsulTestResource implements QuarkusTestResourceLifecycleManager {

    private static final String IMAGE = "consul:1.7";
    private GenericContainer<?> container;

    @Override
    public Map<String, String> start() {
        container = new GenericContainer<>(IMAGE)
                .withExposedPorts(8500, 8501)
                .withCommand("agent", "-dev", "-client=0.0.0.0", "-bind=0.0.0.0", "--https-port=8501")
                .waitingFor(Wait.forLogMessage(".*Synced node info.*", 1));

        container.start();

        return Map.of(
                "consul.host", container.getContainerIpAddress(),
                "consul.port", Integer.toString(container.getMappedPort(8500)),
                "quarkus.stork.red-service.service-registrar.consul-host", container.getContainerIpAddress(),
                "quarkus.stork.red-service.service-registrar.consul-port", Integer.toString(container.getMappedPort(8500))
        );

    }

    @Override
    public void stop() {
        container.stop();
    }
}
