package org.acme.extra;

import io.vertx.core.Vertx;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class SockJsExample {

    @Inject
    Vertx vertx;

    public void init(@Observes Router router) {
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        sockJSHandler.bridge(new SockJSBridgeOptions()
                .addOutboundPermitted(new PermittedOptions().setAddress("ticks")));
        router.route("/eventbus/*").handler(sockJSHandler);

        AtomicInteger counter = new AtomicInteger();
        vertx.setPeriodic(1000,
                ignored -> vertx.eventBus().publish("ticks", counter.getAndIncrement()));
    }

}
