package org.acme.hibernate.reactive;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaQuery;
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
