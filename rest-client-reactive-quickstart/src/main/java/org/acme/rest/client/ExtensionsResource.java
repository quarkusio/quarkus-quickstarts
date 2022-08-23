package org.acme.rest.client;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Set;
import java.util.concurrent.CompletionStage;

@Path("/extension")
public class ExtensionsResource {

    @RestClient
    ExtensionsService extensionsService;

    @Inject MyBean bean;

    @GET
    @Path("/id/{id}")
    @Blocking
    public Set<Extension> id(String id) {
        bean.log();
        return extensionsService.getById(id);
    }

    @GET
    @Path("/id-async/{id}")
    public CompletionStage<Set<Extension>> idAsync(String id) {
        bean.log();
        return extensionsService.getByIdAsync(id)
            .whenComplete((r, f) -> bean.log());
    }

    @GET
    @Path("/id-uni/{id}")
    public Uni<Set<Extension>> idUni(String id) {
        bean.log();
        System.out.println("DC is " + Vertx.currentContext());
        return extensionsService.getByIdAsUni(id)
            .invoke(() -> bean.log()); // PB HERE - request scope suspended and not resumed, so no access.

    }
}
