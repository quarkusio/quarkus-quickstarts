package org.acme.services;

import io.smallrye.mutiny.Uni;
import io.smallrye.stork.api.Metadata;
import io.smallrye.stork.api.ServiceRegistrar;
import io.smallrye.stork.utils.InMemoryAddressesBackend;

import org.jboss.logging.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomServiceRegistrar implements ServiceRegistrar {

    private static final Logger LOGGER = Logger.getLogger(CustomServiceRegistrar.class.getName());

    private final String backendHost;
    private final int backendPort;
    private final Map<String, String> registeredInstances = new ConcurrentHashMap<>();

    public CustomServiceRegistrar(CustomRegistrarConfiguration configuration) {
        this.backendHost = configuration.getHost();
        this.backendPort = Integer.parseInt(configuration.getPort()!=null?configuration.getPort():"8080");
    }


    @Override
    public Uni<Void> registerServiceInstance(String serviceName, Metadata metadata, String ipAddress, int defaultPort) {
        String address = ipAddress + ":" + defaultPort;
        LOGGER.info("Registering service: " + serviceName + " with ipAddress: " + ipAddress + " and port: " + defaultPort);
        registeredInstances.put(serviceName, address);
        InMemoryAddressesBackend.add(serviceName, address);
        return Uni.createFrom().voidItem();
    }

    @Override
    public Uni<Void> deregisterServiceInstance(String serviceName) {
        LOGGER.infof("Deregistering service '%s' from backend %s:%d", serviceName, backendHost, backendPort);
        registeredInstances.remove(serviceName);
        return Uni.createFrom().voidItem();
    }
}