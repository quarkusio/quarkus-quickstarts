package org.acme.reactive.routes;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import io.vertx.ext.web.Router;

@ApplicationScoped
public class MyRouteRegistar {

    public void init(@Observes Router router) {
        router.get("/my-route").handler(rc -> rc.response().end("Hello from my route"));
    }

}
