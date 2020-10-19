package org.acme.hibernate.orm.panache.entity;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.transaction.Transactional;
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

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.quarkus.panache.common.Sort;

@Path("entity/fruits")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class FruitResource {

    @GET
    public List<FruitEntity> get() {
        return FruitEntity.listAll(Sort.by("name"));
    }

    @GET
    @Path("{id}")
    public FruitEntity getSingle(@PathParam Long id) {
        FruitEntity entity = FruitEntity.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(FruitEntity fruitEntity) {
        if (fruitEntity.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        fruitEntity.persist();
        return Response.ok(fruitEntity).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public FruitEntity update(@PathParam Long id, FruitEntity fruitEntity) {
        if (fruitEntity.name == null) {
            throw new WebApplicationException("Fruit Name was not set on request.", 422);
        }

        FruitEntity entity = FruitEntity.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }

        entity.name = fruitEntity.name;

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam Long id) {
        FruitEntity entity = FruitEntity.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Override
        public Response toResponse(Exception exception) {
            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }
            return Response.status(code)
                    .entity(Json.createObjectBuilder().add("error", exception.getMessage()).add("code", code).build())
                    .build();
        }

    }
}
