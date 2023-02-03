package org.acme.ssm;

import java.util.Map;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestQuery;

import io.smallrye.mutiny.Uni;
import software.amazon.awssdk.services.ssm.SsmAsyncClient;

@Path("/async")
public class QuarkusSsmAsyncResource extends QuarkusSsmResource {

    @Inject
    SsmAsyncClient ssm;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Map<String, String>> getParameters() {
        return Uni.createFrom().completionStage(ssm.getParametersByPath(generateGetParametersByPathRequest()))
                .onItem().transform(r -> r.parameters().stream().collect(parametersToMap()));
    }

    @PUT
    @Path("/{name}")
    @Consumes(MediaType.TEXT_PLAIN)
    public Uni<Void> setParameter(String name,
            @RestQuery @DefaultValue("false") boolean secure,
            String value) {

        return Uni.createFrom().completionStage(ssm.putParameter(generatePutParameterRequest(name, value, secure)))
                .onItem().transform(r -> null);
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> getParameter(String name) {
        return Uni.createFrom().completionStage(ssm.getParameter(generateGetParameterRequest(name)))
                .onItem().transform(r -> r.parameter().value());
    }
}