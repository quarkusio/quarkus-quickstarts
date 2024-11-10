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

import software.amazon.awssdk.services.ssm.SsmClient;

@Path("/sync")
public class QuarkusSsmSyncResource extends QuarkusSsmResource {

    @Inject
    SsmClient ssm;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getParameters() {
        return ssm.getParametersByPath(generateGetParametersByPathRequest())
                .parameters().stream().collect(parametersToMap());
    }

    @PUT
    @Path("/{name}")
    @Consumes(MediaType.TEXT_PLAIN)
    public void setParameter(String name,
            @RestQuery @DefaultValue("false") boolean secure,
            String value) {

        ssm.putParameter(generatePutParameterRequest(name, value, secure));
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getParameter(String name) {
        return ssm.getParameter(generateGetParameterRequest(name))
                .parameter().value();
    }
}