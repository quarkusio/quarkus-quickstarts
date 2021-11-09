package org.acme.rest.client;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Set;
import java.util.concurrent.CompletionStage;

@Path("/extensions")
@RegisterRestClient(configKey = "extensions-api")
public interface ExtensionsService {

    @GET
    Set<Extension> getById(@QueryParam("id") String id);

    @GET
    CompletionStage<Set<Extension>> getByIdAsync(@QueryParam("id") String id);

    @GET
    Uni<Set<Extension>> getByIdAsUni(@QueryParam("id") String id);
}
