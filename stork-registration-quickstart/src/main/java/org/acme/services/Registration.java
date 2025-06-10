package org.acme.services;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.stork.Stork;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.concurrent.CountDownLatch;

import static io.smallrye.mutiny.helpers.Subscriptions.fail;

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
        CountDownLatch registrationLatch = new CountDownLatch(1);
        Stork.getInstance().getService("my-service").getServiceRegistrar().registerServiceInstance("red", "localhost",
                red).subscribe()
                .with(success -> registrationLatch.countDown(), failure -> System.out.println("Registration failed"));;
    }
}
