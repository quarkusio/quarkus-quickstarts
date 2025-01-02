package org.acme.extra;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;

@Path("/character-data")
public class ResourceUsingWebClient {

    private final WebClient client;

    public ResourceUsingWebClient(Vertx vertx) {
        this.client = WebClient.create(vertx,
                new WebClientOptions().setDefaultHost("swapi.tech").setDefaultPort(443).setSsl(true)
                        .setTrustAll(true));
    }

    @GET
    @Path("/{id}")
    public Uni<JsonObject> getStarWarsData(String id) {
        return client.get("/api/people/" + id)
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
