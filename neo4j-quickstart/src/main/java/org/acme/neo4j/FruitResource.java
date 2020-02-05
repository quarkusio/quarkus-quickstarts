package org.acme.neo4j;

import java.net.URI;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Values;
import org.neo4j.driver.async.AsyncSession;
import org.neo4j.driver.exceptions.NoSuchRecordException;

@Path("fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FruitResource {

    @Inject
    Driver driver;

    @GET
    public CompletionStage<Response> get() {
        AsyncSession session = driver.asyncSession(); // <1>
        return session
                .runAsync("MATCH (f:Fruit) RETURN f ORDER BY f.name") // <2>
                .thenCompose(cursor -> // <3>
                cursor.listAsync(record -> Fruit.from(record.get("f").asNode())))
                .thenCompose(fruits -> // <4>
                session.closeAsync().thenApply(signal -> fruits))
                .thenApply(Response::ok) // <5>
                .thenApply(ResponseBuilder::build);
    }

    @POST
    public CompletionStage<Response> create(Fruit fruit) {
        AsyncSession session = driver.asyncSession();
        return session
                .writeTransactionAsync(tx -> tx
                        .runAsync("CREATE (f:Fruit {name: $name}) RETURN f", Values.parameters("name", fruit.name))
                        .thenCompose(fn -> fn.singleAsync()))
                .thenApply(record -> Fruit.from(record.get("f").asNode()))
                .thenCompose(persistedFruit -> session.closeAsync().thenApply(signal -> persistedFruit))
                .thenApply(persistedFruit -> Response
                        .created(URI.create("/fruits/" + persistedFruit.id))
                        .build());
    }

    @GET
    @Path("{id}")
    public CompletionStage<Response> getSingle(@PathParam("id") Long id) {
        AsyncSession session = driver.asyncSession();
        return session
                .readTransactionAsync(tx -> tx
                        .runAsync("MATCH (f:Fruit) WHERE id(f) = $id RETURN f", Values.parameters("id", id))
                        .thenCompose(fn -> fn.singleAsync()))
                .handle((record, exception) -> {
                    if (exception != null) {
                        Throwable source = exception;
                        if (exception instanceof CompletionException) {
                            source = ((CompletionException) exception).getCause();
                        }
                        Status status = Status.INTERNAL_SERVER_ERROR;
                        if (source instanceof NoSuchRecordException) {
                            status = Status.NOT_FOUND;
                        }
                        return Response.status(status).build();
                    } else {
                        return Response.ok(Fruit.from(record.get("f").asNode())).build();
                    }
                })
                .thenCompose(response -> session.closeAsync().thenApply(signal -> response));
    }

    @DELETE
    @Path("{id}")
    public CompletionStage<Response> delete(@PathParam("id") Long id) {

        AsyncSession session = driver.asyncSession();
        return session
                .writeTransactionAsync(tx -> tx
                        .runAsync("MATCH (f:Fruit) WHERE id(f) = $id DELETE f", Values.parameters("id", id))
                        .thenCompose(fn -> fn.consumeAsync()))
                .thenCompose(response -> session.closeAsync())
                .thenApply(signal -> Response.noContent().build());
    }
}