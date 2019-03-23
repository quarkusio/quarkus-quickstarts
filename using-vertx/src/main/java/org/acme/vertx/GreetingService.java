package org.acme.vertx;

import io.quarkus.vertx.ConsumeEvent;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class GreetingService {

    @ConsumeEvent("greeting")
    public String greeting(String name) {
        return "Hello " + name;
    }
}
