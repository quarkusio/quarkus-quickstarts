package org.acme.hibernate.reactive;

import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.hibernate.reactive.mutiny.Mutiny.SessionFactory;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.smallrye.mutiny.Uni;

@Path("fruits")
@Produces("application/json")
@Consumes("application/json")
public class FruitMutinyResource {

    @Inject
    SessionFactory factory;

    @GET
    public Uni<List<Fruit>> get() {
        CriteriaQuery<Fruit> query = factory.getCriteriaBuilder().createQuery(Fruit.class);
        query.from(Fruit.class);
        return factory.withStatelessSession(s -> s.createQuery(query).getResultList());
    }

    @GET
    @Path("{id}")
    public Uni<Fruit> getSingle(@RestPath Integer id) {
        return factory.withStatelessSession(s -> s.get(Fruit.class, id));
    }

    @POST
    public Uni<Response> create(Fruit fruit) {
        if (fruit == null || fruit.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        return factory.withStatelessSession(s -> s.insert(fruit))
                .replaceWith(Response.ok(fruit).status(CREATED)::build);
    }

    @PUT
    @Path("{id}")
    public Uni<Response> update(@RestPath Integer id, Fruit fruit) {
        if (fruit == null || fruit.getName() == null) {
            throw new WebApplicationException("Fruit name was not set on request.", 422);
        }

        return factory.withStatelessSession(s -> s.get(Fruit.class, id)
                .onItem().ifNull().failWith(new WebApplicationException("Fruit missing from database.", NOT_FOUND))
                // If entity exists then update it
                .invoke(entity -> entity.setName(fruit.getName()))
                .call(s::update))
                .map(entity -> Response.ok(entity).build());
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@RestPath Integer id) {
        return factory.withStatelessSession(s -> s.get(Fruit.class, id)
                .onItem().ifNull().failWith(new WebApplicationException("Fruit missing from database.", NOT_FOUND))
                // If entity exists then delete it
                .call(s::delete))
                .replaceWith(Response.ok().status(NO_CONTENT)::build);
    }
}
