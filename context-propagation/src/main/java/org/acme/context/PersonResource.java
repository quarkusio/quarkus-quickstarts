package org.acme.context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.vertx.axle.core.Vertx;
import io.vertx.axle.ext.web.client.WebClient;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;

@Path("/people")
public class PersonResource {

    @Inject
    ThreadContext threadContext;

    @Inject
    ManagedExecutor managedExecutor;

    @Inject
    Vertx vertx;

    @Transactional
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<List<Person>> people() {
        // Create a REST client to the Star Wars API
        WebClient client = WebClient.create(vertx,
                                            new WebClientOptions()
                                                    .setDefaultHost("swapi.co")
                                                    .setDefaultPort(443)
                                                    .setSsl(true));

        // get the list of Star Wars people, with context capture
        return threadContext.withContextCapture(client.get("/api/people/").send())
                .thenApplyAsync(response -> {
                    JsonObject json = response.bodyAsJsonObject();
                    List<Person> persons = new ArrayList<>(json.getInteger("count"));
                    // Store them in the DB
                    // Note that we're still in the same transaction as the outer method
                    for (Object element : json.getJsonArray("results")) {
                        Person person = new Person();
                        person.name = ((JsonObject) element).getString("name");
                        person.persist();
                        persons.add(person);
                    }
                    return persons;
                }, managedExecutor);
    }

}
