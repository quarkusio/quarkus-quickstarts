package org.acme.hibernate.reactive;

import static io.quarkus.vertx.web.Route.HandlerType.FAILURE;
import static io.vertx.core.http.HttpMethod.DELETE;
import static io.vertx.core.http.HttpMethod.GET;
import static io.vertx.core.http.HttpMethod.POST;
import static io.vertx.core.http.HttpMethod.PUT;

import java.util.List;
import java.util.NoSuchElementException;

import javax.inject.Inject;

import org.hibernate.reactive.mutiny.Mutiny;
import org.jboss.logging.Logger;

import io.quarkus.vertx.web.Body;
import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * An example using Vert.x Web, Hibernate Reactive and Mutiny.
 */
@RouteBase(path = "/fruits", produces = "application/json")
public class FruitsRoutes {

    private static final Logger LOGGER = Logger.getLogger(FruitsRoutes.class.getName());

    @Inject
    Mutiny.Session session;

    @Route(methods = GET, path = "/")
    public Uni<List<Fruit>> getAll() throws Exception {
        // In this case, it makes sense to return a Uni<List<Fruit>> because we return a reasonable amount of results
        // Consider returning a Multi<Fruit> for result streams 
        return session.createNamedQuery(Fruit.FIND_ALL, Fruit.class).getResultList();
    }

    @Route(methods = GET, path = "/:id")
    public Uni<Fruit> getSingle(@Param String id) {
        return session.find(Fruit.class, Integer.valueOf(id));
    }

    @Route(methods = POST, path = "/")
    public Uni<Fruit> create(@Body Fruit fruit, HttpServerResponse response) {
        if (fruit == null || fruit.getId() != null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Fruit id invalidly set on request."));
        }
        return session.persist(fruit)
                .chain(session::flush)
                .onItem().transform(ignore -> {
                    response.setStatusCode(201);
                    return fruit;
                });
    }

    @Route(methods = PUT, path = "/:id")
    public Uni<Fruit> update(@Body Fruit fruit, @Param String id) {
        if (fruit == null || fruit.getName() == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Fruit name was not set on request."));
        }
        return session.find(Fruit.class, Integer.valueOf(id))
                // If entity exists then update
                .onItem().ifNotNull().transformToUni(entity -> {
                    entity.setName(fruit.getName());
                    return session.flush()
                            .onItem().transform(ignore -> entity);
                })
                // else
                .onItem().ifNull().fail();
    }

    @Route(methods = DELETE, path = "/:id")
    public Uni<Fruit> delete(@Param String id, HttpServerResponse response) {
        return session.find(Fruit.class, Integer.valueOf(id))
                // If entity exists then delete
                .onItem().ifNotNull().transformToUni(entity -> session.remove(entity)
                        .chain(session::flush)
                        .map(ignore -> {
                            response.setStatusCode(204).end();
                            return entity;
                        }))
                // else
                .onItem().ifNull().fail();
    }

    @Route(path = "/*", type = FAILURE)
    public void error(RoutingContext context) {
        Throwable t = context.failure();
        if (t != null) {
            LOGGER.error("Failed to handle request", t);
            int status = context.statusCode();
            String chunk = "";
            if (t instanceof NoSuchElementException) {
                status = 404;
            } else if (t instanceof IllegalArgumentException) {
                status = 422;
                chunk = new JsonObject().put("code", status)
                        .put("exceptionType", t.getClass().getName()).put("error", t.getMessage()).encode();
            }
            context.response().setStatusCode(status).end(chunk);
        } else {
            // Continue with the default error handler
            context.next();
        }
    }

}
