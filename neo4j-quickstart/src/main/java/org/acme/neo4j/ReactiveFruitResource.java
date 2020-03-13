package org.acme.neo4j;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.RxResult;
import org.reactivestreams.Publisher;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("reactivefruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReactiveFruitResource {

    @Inject
    Driver driver;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Publisher<String> get() {
        return Multi.createFrom().resource(
                driver::rxSession,
                session -> session.readTransaction(tx -> {
                    RxResult result = tx.run("MATCH (f:Fruit) RETURN f.name as name ORDER BY f.name");
                    return Multi.createFrom().publisher(result.records())
                            .map(record -> record.get("name").asString());
                })
        ).withFinalizer(session -> {
            return Uni.createFrom().publisher(session.close());
        });
    }
}
