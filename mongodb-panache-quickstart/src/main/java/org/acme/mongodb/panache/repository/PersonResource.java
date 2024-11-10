package org.acme.mongodb.panache.repository;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import org.bson.types.ObjectId;

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
    public Person get(String id) {
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
    public void update(String id, Person person) {
        personRepository.update(person);
    }

    @DELETE
    @Path("/{id}")
    public void delete(String id) {
        Person person = personRepository.findById(new ObjectId(id));
        personRepository.delete(person);
    }

    @GET
    @Path("/search/{name}")
    public Person search(String name) {
        return personRepository.findByName(name);
    }

    @DELETE
    public void deleteAll(){
        personRepository.deleteAll();
    }
}
