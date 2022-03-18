package org.acme.ssm;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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