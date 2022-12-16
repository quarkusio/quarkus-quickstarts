package org.acme.hibernate.orm.panache.rest.entity;

import java.util.List;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.rest.data.panache.MethodProperties;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(hal = true, path = "my-people")
public interface PeopleResource extends PanacheEntityResource<Person, Long> {

    @MethodProperties(path = "all")
    List<Person> list(Page page, Sort sort);

    @MethodProperties(exposed = false)
    boolean delete(Long id);
}
