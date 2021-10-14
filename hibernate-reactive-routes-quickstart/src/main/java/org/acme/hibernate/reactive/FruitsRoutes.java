package org.acme.hibernate.reactive;

import static io.quarkus.vertx.web.Route.HandlerType.FAILURE;
import static io.quarkus.vertx.web.Route.HttpMethod.GET;
import static io.quarkus.vertx.web.Route.HttpMethod.POST;
import static io.quarkus.vertx.web.Route.HttpMethod.PUT;
import static io.quarkus.vertx.web.Route.HttpMethod.DELETE;

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
    Mutiny.SessionFactory sf;

    @Route(methods = GET, path = "/")
    public Uni<List<Fruit>> getAll() {
        return sf.withSession(session -> session
                .createNamedQuery(Fruit.FIND_ALL, Fruit.class)
                .getResultList());
    }

    @Route(methods = GET, path = "/:id")
    public Uni<Fruit> getSingle(@Param Integer id) {
        return sf.withSession(session -> session.find(Fruit.class, id));
    }

    @Route(methods = POST, path = "/")
    public Uni<Fruit> create(@Body Fruit fruit, HttpServerResponse response) {
        if (fruit == null || fruit.getId() != null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Fruit id invalidly set on request."));
        }
        return sf.withTransaction((session, tx) -> session.persist(fruit))
                .invoke(() -> response.setStatusCode(201))
                .replaceWith(fruit);
    }

    @Route(methods = PUT, path = "/:id")
    public Uni<Fruit> update(@Body Fruit fruit, @Param Integer id) {
        if (fruit == null || fruit.getName() == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Fruit name was not set on request."));
        }
        return sf.withTransaction((session,tx) -> session.find(Fruit.class, id)
                // If entity exists then update it
                .onItem().ifNotNull().invoke(entity -> entity.setName(fruit.getName())))
                // If entity not found, fail
                .onItem().ifNull().fail();
    }

    @Route(methods = DELETE, path = "/:id")
    public Uni<Fruit> delete(@Param Integer id, HttpServerResponse response) {
        return sf.withTransaction((session,tx) -> session.find(Fruit.class, id)
                // If entity exists then delete it
                .onItem().ifNotNull()
                    .call(entity -> session.remove(entity)
                            .invoke(() -> response.setStatusCode(204))))
                // If entity not found, fail
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
