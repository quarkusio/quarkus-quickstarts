package org.acme.vertx;

import io.vertx.axle.core.Vertx;
import io.vertx.axle.ext.web.client.WebClient;
import io.vertx.axle.ext.web.codec.BodyCodec;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import java.util.concurrent.CompletionStage;

@Path("/fruit-data")
public class ResourceUsingWebClient {


    @Inject
    Vertx vertx;

    private WebClient client;

    @PostConstruct
    void initialize() {
        this.client = WebClient.create(vertx,
                new WebClientOptions().setDefaultHost("fruityvice.com").setDefaultPort(443).setSsl(true).setTrustAll(true));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{name}")
    public CompletionStage<JsonObject> getFruitData(@PathParam("name") String name) {
        return client.get("/api/fruit/" + name)
                .send()
                .thenApply(resp -> {
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