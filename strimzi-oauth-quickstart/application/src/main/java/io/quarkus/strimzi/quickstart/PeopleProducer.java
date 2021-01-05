package io.quarkus.strimzi.quickstart;

import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PeopleProducer {
    @Outgoing("people-out")
    public Multi<Person> generatePeople() {
        return Multi.createFrom().items(
                new Person("bob"),
                new Person("alice"),
                new Person("tom"),
                new Person("jerry"),
                new Person("anna"),
                new Person("ken"));
    }
}
