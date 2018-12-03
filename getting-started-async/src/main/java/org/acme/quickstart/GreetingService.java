package org.acme.quickstart;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    public CompletionStage<String> greeting(String name) {
        return CompletableFuture.supplyAsync(() -> "hello " + name);
    }

}