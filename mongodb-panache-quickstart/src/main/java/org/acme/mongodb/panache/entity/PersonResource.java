package org.acme.mongodb.panache.entity;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;

@Path("/entity/persons")
@Consumes("application/json")
@Produces("application/json")
public class PersonResource {
    @GET
    public List<Person> list() {
        return Person.listAll();
    }

    @GET
    @Path("/{id}")
    public Person get(String id) {
        return Person.findById(new ObjectId(id));
    }

    @POST
    public Response create(Person person) {
        person.persist();
        return Response.status(201).build();
    }

    @PUT
    @Path("/{id}")
    public void update(String id, Person person) {
        person.update();
    }

    @DELETE
    @Path("/{id}")
    public void delete(String id) {
        Person person = Person.findById(new ObjectId(id));
        person.delete();
    }

    @GET
    @Path("/search/{name}")
    public Person search(String name) {
        return Person.findByName(name);
    }

    @DELETE
    public void deleteAll(){
        Person.deleteAll();
    }
}
