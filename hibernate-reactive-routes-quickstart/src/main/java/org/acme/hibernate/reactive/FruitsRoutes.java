package org.acme.hibernate.reactive;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.hibernate.reactive.mutiny.Mutiny;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import static io.vertx.core.http.HttpMethod.DELETE;
import static io.vertx.core.http.HttpMethod.GET;
import static io.vertx.core.http.HttpMethod.POST;
import static io.vertx.core.http.HttpMethod.PUT;

/**
 * An example using Vert.x Web, Hibernate Reactive and Mutiny.
 */
@ApplicationScoped
@RouteBase(path = "/fruits", produces = "application/json")
public class FruitsRoutes {

    @Inject
    ObjectMapper mapper;

    @Inject
    Mutiny.Session session;

    /**
     * Convert an object into a JSON string.
     */
    private String toJSON(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Route(methods = GET, path = "/")
    public Multi<Fruit> getAll(RoutingContext rc) throws Exception {
        return session.createNamedQuery(Fruit.FIND_ALL, Fruit.class).getResults();
    }

    @Route(methods = GET, path = "/:id")
    public Uni<Fruit> getSingle(RoutingContext rc) {
        final Integer id = Integer.valueOf(rc.request().getParam("id"));

        return session.find(Fruit.class, id);
    }

    @Route(methods = POST, path = "/")
    public Uni<HttpServerResponse> create(RoutingContext rc) {
        final String name = rc.getBodyAsJson().getString("name");
        final Fruit entity = new Fruit(name);

        return session.persist(entity)
                        .onItem().produceUni(s -> session.flush())
                        .onItem().apply(ignore -> httpResponse(rc, 201, entity));
    }

    @Route(methods = PUT, path = "/:id")
    public Uni<HttpServerResponse> update(RoutingContext rc) {
        final Integer id = Integer.valueOf(rc.request().getParam("id"));
        final String name = rc.getBodyAsJson().getString("name");

        return session.find(Fruit.class, id)
                // if entity exists
                .onItem().ifNotNull()
                        .produceUni(entity -> {
                                // Update the entity
                                entity.setName(name);
                                return session.flush()
                                        .onItem().apply(ignore -> httpResponse(rc, 200, entity));
                        })
                // if entity doesn't exist
                .onItem().ifNull()
                        // Not found response code
                        .continueWith(httpResponse(rc, 404));
    }

    @Route(methods = DELETE, path = "/:id")
    public Uni<HttpServerResponse>  delete(RoutingContext rc) {
        final Integer id = Integer.valueOf(rc.request().getParam("id"));

        return session.find(Fruit.class, id)
                // if entity exists
                .onItem().ifNotNull()
                        .produceUni(entity ->
                                // Remove the entity
                                session.remove(entity)
                                        .onItem().produceUni(ignore -> session.flush())
                                        .onItem().apply(ignore -> httpResponse(rc, 204))
                )
                // if entity doesn't exist
                .onItem().ifNull()
                        // Not found response code
                        .continueWith(httpResponse(rc, 404));
    }

    protected static HttpServerResponse httpResponse(RoutingContext rc, int statusCode) {
        return rc.response()
                .setStatusCode(statusCode)
                .putHeader("Content-Type", "application/json");
    }

    /**
     * Generate the response with the specified status code and add the object to the body as JSON.
     */
    protected HttpServerResponse httpResponse(RoutingContext rc, int statusCode, Object object) {
        String asJson = toJSON(object);
        return httpResponse(rc, statusCode)
                // Content-Length header is required when using .write
                .putHeader("Content-Length", Long.toString(asJson.length()))
                .write(asJson);
    }
}
