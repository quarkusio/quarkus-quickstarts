package org.acme.vertx;

import io.quarkus.vertx.ConsumeEvent;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class GreetingService {

    @ConsumeEvent("greeting")
    CompletionStage<String> consume(String name) {
        return CompletableFuture
                .supplyAsync(name::toUpperCase);
    }
}
