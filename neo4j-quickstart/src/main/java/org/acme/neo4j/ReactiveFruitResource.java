package org.acme.neo4j;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.RxResult;
import org.neo4j.driver.reactive.RxSession;
import org.reactivestreams.Publisher;

import reactor.core.publisher.Flux;

@Path("reactivefruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReactiveFruitResource {

    @Inject
    Driver driver;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Publisher<String> get() {
        return Flux.using(driver::rxSession, session -> session.readTransaction(tx -> {            
            RxResult result = tx.run("MATCH (f:Fruit) RETURN f.name as name ORDER BY f.name");
            return Flux.from(result.records()).map(record -> record.get("name").asString());
        }), RxSession::close);
    }
}
