package org.acme.rest.client;

import java.util.Set;
import java.util.concurrent.CompletionStage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import io.smallrye.mutiny.Uni;

@Path("/extensions")
@RegisterRestClient
@RegisterClientHeaders(RequestUUIDHeaderFactory.class)
public interface ExtensionsService {

    @GET
    Set<Extension> getById(@QueryParam String id);

    @GET
    CompletionStage<Set<Extension>> getByIdAsync(@QueryParam String id);

    @GET
    Uni<Set<Extension>> getByIdAsUni(@QueryParam String id);
}
