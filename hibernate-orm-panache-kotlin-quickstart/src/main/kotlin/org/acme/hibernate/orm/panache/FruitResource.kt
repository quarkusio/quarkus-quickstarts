package org.acme.hibernate.orm.panache

import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.panache.common.Sort
import org.jboss.logging.Logger
import org.jboss.resteasy.reactive.RestPath;
import java.lang.Exception
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Path("fruits")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
class FruitResource {
    @GET
    fun get(): List<Fruit> {
        return Fruit.listAll(Sort.by("name"))
    }

    @GET
    @Path("{id}")
    fun getSingle(@RestPath id: Long): Fruit {
        return Fruit.findById(id)
            ?: throw WebApplicationException("Fruit with id of $id does not exist.", 404)
    }

    @POST
    @Transactional
    fun create(fruit: Fruit): Response {
        if (fruit.id != null) {
            throw WebApplicationException("Id was invalidly set on request.", 422)
        }
        fruit.persist()
        return Response.ok(fruit).status(201).build()
    }

    @PUT
    @Path("{id}")
    @Transactional
    fun update(@RestPath id: Long, fruit: Fruit): Fruit {
        val entity: Fruit = Fruit.findById(id)
            ?: throw WebApplicationException("Fruit with id of $id does not exist.", 404)
        entity.name = fruit.name
        return entity
    }

    @DELETE
    @Path("{id}")
    @Transactional
    fun delete(@RestPath id: Long): Response {
        val entity: Fruit = Fruit.findById(id)
            ?: throw WebApplicationException("Fruit with id of $id does not exist.", 404)
        entity.delete()
        return Response.status(204).build()
    }

    @Provider
    class ErrorMapper : ExceptionMapper<Exception> {
        @Inject
        var objectMapper: ObjectMapper? = null

        override fun toResponse(exception: Exception): Response {
            LOGGER.error("Failed to handle request", exception)
            var code = 500
            if (exception is WebApplicationException) {
                code = exception.response.status
            }
            val exceptionJson = objectMapper!!.createObjectNode()
            exceptionJson.put("exceptionType", exception.javaClass.name)
            exceptionJson.put("code", code)
            if (exception.message != null) {
                exceptionJson.put("error", exception.message)
            }
            return Response.status(code)
                .entity(exceptionJson)
                .build()
        }
    }

    companion object {
        private val LOGGER = Logger.getLogger(FruitResource::class.java.name)
    }
}