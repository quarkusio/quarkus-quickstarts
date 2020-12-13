package org.acme.optaplanner.persistence;

import javax.enterprise.context.ApplicationScoped;

import org.acme.optaplanner.domain.Room;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class RoomRepository implements PanacheRepository<Room> {

}
