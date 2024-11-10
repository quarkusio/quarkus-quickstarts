package org.acme.optaplanner.rest;

import org.acme.optaplanner.domain.Room;
import org.acme.optaplanner.persistence.RoomRepository;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "rooms")
public interface RoomResource extends PanacheRepositoryResource<RoomRepository, Room, Long> {

}
