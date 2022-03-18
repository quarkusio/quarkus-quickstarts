package org.acme;

import java.nio.charset.StandardCharsets;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestQuery;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;

@Path("/vertx")                        // <1>
public class VertxResource {

    private final Vertx vertx;
    private final WebClient client;

    VertxResource(Vertx vertx) { // <2>
        this.vertx = vertx;
        this.client = WebClient.create(vertx);
    }


    @GET
    @Path("/lorem")
    public Uni<String> readShortFile() {
        return vertx.fileSystem().readFile("lorem.txt")
                .onItem().transform(content -> content.toString(StandardCharsets.UTF_8));
    }


    @GET
    @Path("/book")
    public Multi<String> readLargeFile() {
        return vertx.fileSystem().open("book.txt", new OpenOptions().setRead(true))
                .onItem().transformToMulti(file -> file.toMulti())
                .onItem().transform(content -> content.toString(StandardCharsets.UTF_8)
                        + "\n------------\n");
    }

    @Inject
    EventBus bus;

    @GET
    @Path("/hello")
    public Uni<String> hello(@RestQuery String name) {
        return bus.<String>request("greetings", name)
                .onItem().transform(response -> response.body());
    }

    @GET
    @Path("/web")
    public Uni<JsonArray> retrieveDataFromWikipedia() {
        String url = "https://en.wikipedia.org/w/api.php?action=parse&page=Quarkus&format=json&prop=langlinks";
        return client.getAbs(url).send()
                .onItem().transform(HttpResponse::bodyAsJsonObject)
                .onItem().transform(json -> json.getJsonObject("parse").getJsonArray("langlinks"));
    }


}
