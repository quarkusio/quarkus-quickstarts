package org.acme.services;

import io.smallrye.mutiny.Uni;
import io.smallrye.stork.api.Metadata;
import io.smallrye.stork.api.ServiceRegistrar;

import org.jboss.logging.Logger;

public class CustomServiceRegistrar implements ServiceRegistrar {

    private static final Logger LOGGER = Logger.getLogger(CustomServiceRegistrar.class.getName());


    private final String backendHost;
    private final int backendPort;

    public CustomServiceRegistrar(CustomRegistrarConfiguration configuration) {
        this.backendHost = configuration.getHost();
        this.backendPort = Integer.parseInt(configuration.getPort()!=null?configuration.getPort():"8080");
    }


    @Override
    public Uni<Void> registerServiceInstance(String serviceName, Metadata metadata, String ipAddress, int defaultPort) {
        //do whatever is needed for registering service instance
        LOGGER.info("Registering service: " + serviceName + " with ipAddress: " + ipAddress + " and port: " + defaultPort);
        return Uni.createFrom().voidItem();
    }

    @Override
    public Uni<Void> deregisterServiceInstance(String serviceName) {
        return Uni.createFrom().voidItem();
    }
}