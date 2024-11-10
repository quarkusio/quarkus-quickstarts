package org.acme;


import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.InternetProtocol;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.Map;

public class DnsTestResource implements QuarkusTestResourceLifecycleManager {

    private static final String IMAGE = "consul:1.7";
    private GenericContainer<?> container;

    @Override
    public Map<String, String> start() {

        container = new FixedHostPortGenericContainer<>(IMAGE)
                .withFixedExposedPort(8601, 8600, InternetProtocol.UDP)
                .withExposedPorts(8500, 8501)
                .withCommand("agent", "-dev", "-client=0.0.0.0", "-bind=0.0.0.0", "--https-port=8501")
                .withLogConsumer(of -> System.out.print(of.getUtf8String()))
                .waitingFor(Wait.forLogMessage(".*Synced node info.*", 1));


        container.start();


        return Map.of(
                "consul.host", container.getHost(),
                "consul.port", Integer.toString(container.getMappedPort(8500)),
                "quarkus.stork.my-service.service-discovery.dns-servers", container.getHost() + ":" + 8601
        );
    }

    @Override
    public void stop() {
        container.stop();
    }
}
