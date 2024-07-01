package org.acme.hibernate.reactive.panache.rest.entity;

import java.util.List;

import io.quarkus.hibernate.reactive.rest.data.panache.PanacheEntityResource;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.rest.data.panache.MethodProperties;
import io.quarkus.rest.data.panache.ResourceProperties;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import java.util.Collections;

@ResourceProperties(hal = true, path = "my-people")
public interface PeopleResource extends PanacheEntityResource<Person, Long> {

    @MethodProperties(path = "all")
    Uni<List<Person>> list(Page page, Sort sort);

    @MethodProperties(exposed = false)
    Uni<Boolean> delete(Long id);

    @GET
    @Path("/name/{name}")
    @Produces("application/json")
    default Uni<List<Person>> findByName(@PathParam("name") String name) {
        return Person.find("name = :name", Collections.singletonMap("name", name)).list();
    }
}
