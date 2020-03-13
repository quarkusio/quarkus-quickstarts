package org.acme.getting.started.async;

import javax.enterprise.context.ApplicationScoped;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

@ApplicationScoped
public class GreetingService {

    public Uni<String> greeting(String name) {
        return Uni.createFrom().item("hello " + name)
                .emitOn(Infrastructure.getDefaultExecutor());
    }

}
