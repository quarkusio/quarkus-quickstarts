package org.acme.optaplanner.rest;

import org.acme.optaplanner.domain.Timeslot;
import org.acme.optaplanner.persistence.TimeslotRepository;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "timeslots")
public interface TimeslotResource extends PanacheRepositoryResource<TimeslotRepository, Timeslot, Long> {

}
