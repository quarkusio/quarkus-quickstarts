package org.acme.mongodb.panache.repository;

import org.bson.types.ObjectId;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/repository/persons")
@Consumes("application/json")
@Produces("application/json")
public class PersonResource {
    @Inject
    PersonRepository personRepository;

    @GET
    public List<Person> list() {
        return personRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Person get(@PathParam("id") String id) {
        System.out.println("get =>" + id);
        return personRepository.findById(new ObjectId(id));
    }

    @POST
    public Response create(Person person) {
        personRepository.persist(person);
        return Response.status(201).build();
    }

    @PUT
    @Path("/{id}")
    public void update(@PathParam("id") String id, Person person) {
        personRepository.update(person);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id) {
        Person person = personRepository.findById(new ObjectId(id));
        personRepository.delete(person);
    }

    @GET
    @Path("/search/{name}")
    public Person search(@PathParam("name") String name) {
        return personRepository.findByName(name);
    }

    @DELETE
    public void deleteAll(){
        personRepository.deleteAll();
    }
}
