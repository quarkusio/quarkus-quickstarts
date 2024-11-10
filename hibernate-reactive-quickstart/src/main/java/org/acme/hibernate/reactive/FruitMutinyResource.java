package org.acme.hibernate.reactive;

import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory;

@Path("fruits")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class FruitMutinyResource {

    @Inject
    SessionFactory sf;

    @GET
    public Uni<List<Fruit>> get() {
        return sf.withTransaction((s,t) -> s
                .createNamedQuery("Fruits.findAll", Fruit.class)
                .getResultList()
        );
    }

    @GET
    @Path("{id}")
    public Uni<Fruit> getSingle(Integer id) {
        return sf.withTransaction((s,t) -> s.find(Fruit.class, id));
    }

    @POST
    public Uni<Response> create(Fruit fruit) {
        if (fruit == null || fruit.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        return sf.withTransaction((s,t) -> s.persist(fruit))
                .replaceWith(Response.ok(fruit).status(CREATED)::build);
    }

    @PUT
    @Path("{id}")
    public Uni<Response> update(Integer id, Fruit fruit) {
        if (fruit == null || fruit.getName() == null) {
            throw new WebApplicationException("Fruit name was not set on request.", 422);
        }

        return sf.withTransaction((s,t) -> s.find(Fruit.class, id)
                .onItem().ifNull().failWith(new WebApplicationException("Fruit missing from database.", NOT_FOUND))
                // If entity exists then update it
                .invoke(entity -> entity.setName(fruit.getName())))
                .map(entity -> Response.ok(entity).build());
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(Integer id) {
        return sf.withTransaction((s,t) -> s.find(Fruit.class, id)
                .onItem().ifNull().failWith(new WebApplicationException("Fruit missing from database.", NOT_FOUND))
                // If entity exists then delete it
                .call(s::remove))
                .replaceWith(Response.ok().status(NO_CONTENT)::build);    }

}
