package org.acme.hibernate.reactive;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

import org.hibernate.reactive.mutiny.Mutiny.Session;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.smallrye.mutiny.Uni;

@Path("fruits")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class FruitMutinyResource {

    private static final Response HTTP404 = Response.ok().status(404).build();

    @Inject
    Session session;

    @GET
    public Uni<List<Fruit>> get() {
        return session
                .createNamedQuery("Fruits.findAll", Fruit.class)
                .getResultList();
    }

    @GET
    @Path("{id}")
    public Uni<Fruit> getSingle(@RestPath Integer id) {
        return session.find(Fruit.class, id);
    }

    @POST
    public Uni<Response> create(Fruit fruit) {
        if (fruit == null || fruit.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        return session
                .persist(fruit)
                .call(session::flush)
                .replaceWith(Response.ok(fruit).status(201)::build);
    }

    @PUT
    @Path("{id}")
    public Uni<Response> update(@RestPath Integer id, Fruit fruit) {
        if (fruit == null || fruit.getName() == null) {
            throw new WebApplicationException("Fruit name was not set on request.", 422);
        }

        return session
                .find(Fruit.class, id)
                    // If no entity exists then
                    .onItem().ifNull().fail()
                    // else
                    .invoke(entity -> entity.setName(fruit.getName()))
                    .call(session::flush)
                    .map(entity -> Response.ok(entity).build())
                    .onFailure().recoverWithItem(HTTP404);
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@RestPath Integer id) {
        return session
                .find(Fruit.class, id)
                    // If no entity exists then
                    .onItem().ifNull().fail()
                    // else
                    .call(session::remove)
                    .call(session::flush)
                    .replaceWith(Response.ok().status(204).build())
                    .onFailure().recoverWithItem(HTTP404);
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
        private static final Logger LOGGER = Logger.getLogger(FruitMutinyResource.class);

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code).entity(exceptionJson).build();
        }

    }
}
