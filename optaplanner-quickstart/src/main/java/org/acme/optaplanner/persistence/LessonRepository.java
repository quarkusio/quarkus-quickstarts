package org.acme.optaplanner.persistence;

import jakarta.enterprise.context.ApplicationScoped;

import org.acme.optaplanner.domain.Lesson;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class LessonRepository implements PanacheRepository<Lesson> {

}
