package org.acme.getting.started.async;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    public Uni<String> greeting(String name) {
        return Uni.createFrom().item("hello " + name)
                .emitOn(Infrastructure.getDefaultExecutor());
    }

}