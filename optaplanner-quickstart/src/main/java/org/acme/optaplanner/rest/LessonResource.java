package org.acme.optaplanner.rest;

import org.acme.optaplanner.domain.Lesson;
import org.acme.optaplanner.persistence.LessonRepository;

import io.quarkus.hibernate.orm.rest.data.panache.PanacheRepositoryResource;
import io.quarkus.rest.data.panache.ResourceProperties;

@ResourceProperties(path = "lessons")
public interface LessonResource extends PanacheRepositoryResource<LessonRepository, Lesson, Long> {

}
