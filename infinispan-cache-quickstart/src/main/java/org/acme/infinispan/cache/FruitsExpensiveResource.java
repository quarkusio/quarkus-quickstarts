package org.acme.infinispan.cache;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.Proto;
import org.infinispan.protostream.annotations.ProtoSchema;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Path("/fruits")
public class FruitsExpensiveResource {

    private final AtomicInteger invocations = new AtomicInteger(0);

    @GET
    @Path("/{country}/{city}/{name}")
    @CacheResult(cacheName = "fruits")
    public ExpensiveFruitsResponse getExpensiveFruitsResponse(@PathParam("country") @CacheKey String country,
                                                              @PathParam("city") @CacheKey String city,
                                                              @PathParam("name") @CacheKey String name,
                                                              @QueryParam("metadata") String metadata) {
        invocations.incrementAndGet();
        String id = UUID.randomUUID().toString();
        String description = String.format("Fruit in city %s, with name %s", city, name);
        return new ExpensiveFruitsResponse(id, description, metadata);
    }

    @GET
    @Path("/invocations")
    public int getInvocations() {
        return invocations.get();
    }

    @Proto
    public record ExpensiveFruitsResponse(String id, String description, String metadata) {
    }

    @ProtoSchema(includeClasses = { ExpensiveFruitsResponse.class }, schemaFileName = "fruits.proto")
    interface Schema extends GeneratedSchema {
    }
}
