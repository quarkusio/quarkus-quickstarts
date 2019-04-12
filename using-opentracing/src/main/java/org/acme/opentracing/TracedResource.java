package org.acme.opentracing;

import java.net.URI;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

@Path("/")
public class TracedResource {

    @Inject
    private FrancophoneService exampleBean;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    @GET
    @Path("/chain")
    @Produces(MediaType.TEXT_PLAIN)
    public String chain() {
        ResourceClient resourceClient = RestClientBuilder.newBuilder()
            .baseUri(uriInfo.getBaseUri())
            .build(ResourceClient.class);
        return "chain -> " + exampleBean.bonjour() + " -> " + resourceClient.hello();
    }
}
