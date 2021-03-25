package org.acme.vertx;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;

@Path("/fruit-data")
public class ResourceUsingWebClient {

    private final WebClient client;

    public ResourceUsingWebClient(Vertx vertx) {
        this.client = WebClient.create(vertx,
                new WebClientOptions().setDefaultHost("fruityvice.com").setDefaultPort(443).setSsl(true)
                        .setTrustAll(true));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{name}")
    public Uni<JsonObject> getFruitData(@PathParam("name") String name) {
        return client.get("/api/fruit/" + name)
                .send()
                .map(resp -> {
                    if (resp.statusCode() == 200) {
                        return resp.bodyAsJsonObject();
                    } else {
                        return new JsonObject()
                                .put("code", resp.statusCode())
                                .put("message", resp.bodyAsString());
                    }
                });
    }

}
