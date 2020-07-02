package org.acme.hibernate.reactive;

import java.util.List;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.hibernate.reactive.mutiny.Mutiny;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@Path("fruits")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class FruitMutinyResource {
    private static final Logger LOGGER = Logger.getLogger(FruitMutinyResource.class.getName());

    @Inject
    Mutiny.Session mutinySession;

    @GET
    public Multi<Fruit> get() {
        return mutinySession
                .createNamedQuery( "Fruits.findAll", Fruit.class ).getResults();
    }

    @GET
    @Path("{id}")
    public Uni<Fruit> getSingle(@PathParam Integer id) {
        return mutinySession.find(Fruit.class, id);
    }

    @POST
    public Uni<Response> create(Fruit fruit) {
        if (fruit == null || fruit.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        return mutinySession
                .persist(fruit)
                .onItem().produceUni(session -> mutinySession.flush())
                .onItem().apply(ignore -> Response.ok(fruit).status(201).build());
    }

    @PUT
    @Path("{id}")
    public Uni<Response> update(@PathParam Integer id, Fruit fruit) {
        if (fruit == null || fruit.getName() == null) {
            throw new WebApplicationException("Fruit name was not set on request.", 422);
        }

        // Update function (never returns null)
        Function<Fruit, Uni<Response>> update = entity -> {
            entity.setName(fruit.getName());
            return mutinySession.flush()
                    .onItem().apply(ignore -> Response.ok(entity).build());
        };

        return mutinySession
                .find( Fruit.class, id )
                      // If entity exists then
                    .onItem().ifNotNull()
                        .produceUni(update)
                    // else
                    .onItem().ifNull()
                        .continueWith(Response.ok().status(404).build());
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@PathParam Integer id) {
        // Delete function (never returns null)
        Function<Fruit, Uni<Response>> delete = entity -> mutinySession.remove(entity)
                .onItem().produceUni(ignore -> mutinySession.flush())
                .onItem().apply(ignore -> Response.ok().status(204).build());

        return mutinySession
                .find( Fruit.class, id )
                    // If entity exists then
                    .onItem().ifNotNull()
                        .produceUni(delete)
                    // else
                    .onItem().ifNull()
                        .continueWith(Response.ok().status(404).build());
    }

    /**
     * Create a HTTP response from an exception.
     *
     * Response Example:
     *
     * <pre>
     * HTTP/1.1 422 Unprocessable Entity
     * Content-Length: 111
     * Content-Type: application/json
     *
     * {
     *     "code": 422,
     *     "error": "Fruit name was not set on request.",
     *     "exceptionType": "javax.ws.rs.WebApplicationException"
     * }
     * </pre>
     */
    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            JsonObjectBuilder entityBuilder = Json.createObjectBuilder()
                    .add("exceptionType", exception.getClass().getName())
                    .add("code", code);

            if (exception.getMessage() != null) {
                entityBuilder.add("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(entityBuilder.build())
                    .build();
        }

    }
}
