
package org.acme;

import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.core.Vertx;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;


/***
 *  A simple red service exposed on port 9001.
 *
 * This is only for testing purpose. As we are using Kubernetes as service discovery, we cannot run this application in the Kubernetes mock server.
 * It runs locally and configure the Kuberentes endpoint to return `localhost` as IP address.
 */
@ApplicationScoped
public class RedService {
    @ConfigProperty(name = "red-service-port", defaultValue = "9001") int port;

    /**
     * Start an HTTP server for the red service, it sends a response with "Hello from Red!" as the body.
     *
     * Note: this method is called on a worker thread, and so it is allowed to block.
     */
    public void init(@Observes StartupEvent ev, Vertx vertx) {
        vertx.createHttpServer()
                .requestHandler(req -> req.response().endAndForget("Hello from Red!"))
                .listenAndAwait(port);
    }

}
