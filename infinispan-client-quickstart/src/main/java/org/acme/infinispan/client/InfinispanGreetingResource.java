package org.acme.infinispan.client;

import io.quarkus.infinispan.client.Remote;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.infinispan.client.hotrod.RemoteCache;

import java.util.concurrent.CompletionStage;

@Path("/greeting")
public class InfinispanGreetingResource {

    @Inject
    @Remote("mycache")
    RemoteCache<String, Greeting> cache;

    @POST
    @Path("/{id}")
    public CompletionStage<String> postGreeting(String id, Greeting greeting) {
        return cache.putAsync(id, greeting)
              .thenApply(g -> "Greeting added!")
              .exceptionally(ex -> ex.getMessage());
    }

    @GET
    @Path("/{id}")
    public CompletionStage<Greeting> getGreeting(String id) {
        return cache.getAsync(id);
    }
}
