package org.acme.hibernate.orm.panache

import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.panache.common.Sort
import org.jboss.logging.Logger
import org.jboss.resteasy.reactive.RestPath;
import java.lang.Exception
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

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
        lateinit var objectMapper: ObjectMapper

        override fun toResponse(exception: Exception): Response {
            LOGGER.error("Failed to handle request", exception)
            var code = 500
            if (exception is WebApplicationException) {
                code = exception.response.status
            }
            val exceptionJson = objectMapper.createObjectNode()
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