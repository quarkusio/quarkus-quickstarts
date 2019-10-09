package org.acme.quarkus.sample;

import io.vertx.ext.web.Router;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class MyRouteRegistar {

    public void init(@Observes Router router) {
        router.get("/my-route").handler(rc -> rc.response().end("Hello from my route"));
    }


}
