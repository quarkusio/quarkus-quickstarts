package org.acme.hibernate.orm.panache.entity;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

import org.acme.hibernate.orm.panache.repository.Fruit;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.quarkus.panache.common.Sort;

@Path("entity/fruits")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class FruitEntityResource {

    private static final Logger LOGGER = Logger.getLogger(FruitEntityResource.class.getName());

    @GET
    public List<FruitEntity> get() {
        return FruitEntity.listAll(Sort.by("name"));
    }

    @GET
    @Path("{id}")
    public FruitEntity getSingle(Long id) {
        FruitEntity entity = FruitEntity.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(FruitEntity fruit) {
        if (fruit.id != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        fruit.persist();
        return Response.ok(fruit).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public FruitEntity update(Long id, Fruit fruit) {
        if (fruit.name == null) {
            throw new WebApplicationException("Fruit Name was not set on request.", 422);
        }

        FruitEntity entity = FruitEntity.findById(id);

        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }

        entity.name = fruit.name;

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Long id) {
        FruitEntity entity = FruitEntity.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        entity.delete();
        return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

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

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}
