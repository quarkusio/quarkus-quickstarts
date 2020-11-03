package org.acme.hibernate.orm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
//import javax.json.Json;
//import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Transactional(Transactional.TxType.REQUIRED)
@Path("fruits")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class FruitResource extends BaseResource<Fruit>{

    private static final Logger LOGGER = Logger.getLogger(FruitResource.class.getName());

//    @Inject
//    EntityManagerFactory entityManagerFactory;

    public FruitResource() {
        super(Fruit.class);
    }

    @GET
    public List<Fruit> get() {
//        return entityManager.createNamedQuery("Fruits.findAll", entityClass)
//                .getResultList();
        EntityManager entityManager = createEntityManager();
        return findAll(entityManager);
    }

    @GET
    @Path("{id}")
    public Fruit getSingle(@PathParam Integer id) {
//        Fruit entity = entityManager.find(entityClass, id);
//        if (entity == null) {
//            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
//        }
//        return entity;
        EntityManager entityManager = createEntityManager();
        Fruit fruit = find(entityManager, id, entityClass);
        return fruit;
    }

    @POST
    @Transactional
    public Response create(Fruit fruit) {
        if (fruit.getId() != null) {
            throw new WebApplicationException("Id was invalidly set on request.", 422);
        }

        createEntityManager().persist(fruit);
        return Response.ok(fruit).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Fruit update(@PathParam Integer id, Fruit fruit) {
        if (fruit.getName() == null) {
            throw new WebApplicationException("Fruit Name was not set on request.", 422);
        }

        Fruit entity = entityManagerFactory.createEntityManager().find(entityClass, id);

        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }

        entity.setName(fruit.getName());

        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam Integer id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Fruit entity = entityManager.getReference(entityClass, id);
        if (entity == null) {
            throw new WebApplicationException("Fruit with id of " + id + " does not exist.", 404);
        }
        entityManager.remove(entity);
        return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

//            JsonObjectBuilder entityBuilder = Json.createObjectBuilder()
//                    .add("exceptionType", exception.getClass().getName())
//                    .add("code", code);
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("exceptionType", exception.getClass().getName());
            objectNode.put("code", code);

            if (exception.getMessage() != null) {
//                entityBuilder.add("error", exception.getMessage());
                objectNode.put("error", exception.getMessage());
            }

//            return Response.status(code)
//                    .entity(entityBuilder.build())
//                    .build();
            try {
                return Response.status(code)
                        .entity(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode))
                        .build();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }

    }
}
