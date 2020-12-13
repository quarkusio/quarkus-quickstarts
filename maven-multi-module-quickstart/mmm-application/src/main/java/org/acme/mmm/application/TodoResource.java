package org.acme.mmm.application;

import org.acme.mmm.domain.Todo;
import org.acme.mmm.domain.TodoRepostory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/todo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.TEXT_PLAIN)
public class TodoResource {

    @Inject
    TodoRepostory todoRepostory;

    @GET
    @Path("/{id}")
    public Todo getTodoById(@PathParam("id") long id) {
        return todoRepostory.todoById(id)
                .orElseThrow(() -> new WebApplicationException(Response.Status.NOT_FOUND));
    }

    @POST
    public Todo addTodo(String todoText) {
        return todoRepostory.saveTodo(todoText);
    }
}
