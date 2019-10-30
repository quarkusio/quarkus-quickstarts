package org.acme.quarkus.sample;

import io.quarkus.vertx.http.runtime.filters.Filters;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class MyFilter {

    public void registerMyFilter(@Observes Filters filters) {
        filters.register(rc -> {
            rc.response().putHeader("X-Header", "intercepting the request");
            rc.next();
        }, 100);
    }
}
