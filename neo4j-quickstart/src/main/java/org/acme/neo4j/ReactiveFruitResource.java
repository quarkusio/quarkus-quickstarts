package org.acme.neo4j;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.RxResult;
import org.neo4j.driver.reactive.RxSession;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("reactivefruits")
public class ReactiveFruitResource {

    @Inject
    Driver driver;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> get() {
        return Multi.createFrom().<RxSession, String>resource(
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
