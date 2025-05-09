package org.acme.services;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.stork.Stork;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Registration {

    @ConfigProperty(name = "red-service-port", defaultValue = "9000")
    int red;

    /**
     * Register our Red service using consul list.
     *
     * Note: this method is called on a worker thread, and so it is allowed to block.
     */
    public void init(@Observes StartupEvent ev, Vertx vertx) {
        Stork.getInstance().getService("my-service").getServiceRegistrar().registerServiceInstance("my-service", "localhost",
                red);
    }
}
