package org.acme.services;

import io.smallrye.stork.api.Metadata;
import io.smallrye.stork.api.ServiceRegistrar;
import io.smallrye.stork.api.config.ServiceRegistrarAttribute;
import io.smallrye.stork.api.config.ServiceRegistrarType;
import io.smallrye.stork.spi.ServiceRegistrarProvider;
import io.smallrye.stork.spi.StorkInfrastructure;
import jakarta.enterprise.context.ApplicationScoped;

@ServiceRegistrarType(value = "custom", metadataKey = Metadata.DefaultMetadataKey.class)
@ServiceRegistrarAttribute(name = "host",
        description = "Host name of the service registration server.", required = true)
@ServiceRegistrarAttribute(name = "port",
        description = "Port of the service registration server.", required = false)
@ApplicationScoped
public class CustomServiceRegistrarProvider
        implements ServiceRegistrarProvider<CustomRegistrarConfiguration, Metadata.DefaultMetadataKey> {

    @Override
    public ServiceRegistrar createServiceRegistrar(
            CustomRegistrarConfiguration config,
            String serviceName,
            StorkInfrastructure storkInfrastructure) {
        return new CustomServiceRegistrar(config);
    }

}


