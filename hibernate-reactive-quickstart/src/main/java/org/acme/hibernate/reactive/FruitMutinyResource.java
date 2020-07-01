package org.acme.hibernate.reactive;

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

import org.hibernate.reactive.mutiny.Mutiny;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.smallrye.mutiny.Uni;

@Path("fruits")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class FruitMutinyResource {

    @Inject
    Uni<Mutiny.Session> mutinySession;

    @GET
    public Uni<Fruit[]> get() {
        return mutinySession.flatMap( session -> session.createNamedQuery( "Fruits.findAll", Fruit.class ).getResultList() )
                .map( fruits -> fruits.toArray( new Fruit[fruits.size()] ) );
    }

    @GET
    @Path("{id}")
    public Uni<Fruit> getSingle(@PathParam Integer id) {
        return mutinySession
                .flatMap( session -> session.find( Fruit.class, id ) );
    }

    @POST
    public Uni<Response> create(Fruit fruit) {
        if (fruit.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }
        return mutinySession
                .flatMap( session -> session.persist( fruit ) )
                .flatMap( session -> session.flush() )
                .map( ignore -> Response.ok( fruit ).status( 201 ).build() );
    }

    @PUT
    @Path("{id}")
    public Uni<Fruit>  update(@PathParam Integer id, Fruit fruit) {
        if (fruit.getName() == null) {
            throw new WebApplicationException("Fruit Name was not set on request.", 422);
        }

        return mutinySession
                .flatMap( session -> session.find( Fruit.class, id )
                        .flatMap( entity -> {
                            if (entity == null) {
                                throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
                            }
                            entity.setName( fruit.getName() );
                            return session.flush()
                                    .map( ignore -> entity );
                        } )
                );
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@PathParam Integer id) {
        return mutinySession
                .flatMap( session -> session.find( Fruit.class, id )
                        .flatMap( entity -> {
                            if ( entity == null ) {
                                throw new WebApplicationException( "Fruit with id of " + id + " does not exist.", 404 );
                            }
                            return session.remove( entity );
                        } )
                )
                .flatMap( session -> session.flush() )
                .map( ignore -> Response.ok().status( 204 ).build() );
    }
}
