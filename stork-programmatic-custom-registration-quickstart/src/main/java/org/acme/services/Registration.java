package org.acme.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.stork.Stork;
import io.vertx.mutiny.core.Vertx;

@ApplicationScoped
public class Registration {

    /**
     * Register our two services using custom registrar.
     *
     * Note: this method is called on a worker thread, and so it is allowed to block.
     */
    public void init(@Observes StartupEvent ev, Vertx vertx) {
        Stork.getInstance().getService("my-service").registerInstance("my-service", "localhost",
                9000);
    }
}
