package io.quarkus.strimzi.quickstart;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.connectors.InMemorySink;
import io.smallrye.reactive.messaging.connectors.InMemorySource;
import org.junit.jupiter.api.Test;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@QuarkusTestResource(KafkaTestResourceLifecycleManager.class)
public class PeopleApplicationTest {

    @Inject
    @Any
    InMemoryConnector connector;

    @Inject
    PeopleManager people;

    @Test
    void testPeopleManager() {
        InMemorySource<Person> in = connector.source("people-in");
        assertThat(people.getPeople()).isEmpty();

        in.send(new Person("a"));
        in.send(new Person("b"));
        in.send(new Person("c"));

        assertThat(people.getPeople()).hasSize(3)
                .allSatisfy(p -> assertThat(p.getName()).isIn("a", "b", "c"));
    }

    @Test
    void testGeneration() {
        InMemorySink<Person> out = connector.sink("people-out");
        assertThat(out.received()).hasSize(6);
    }

}
