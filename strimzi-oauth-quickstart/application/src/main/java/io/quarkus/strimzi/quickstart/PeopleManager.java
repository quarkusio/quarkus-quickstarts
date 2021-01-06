package io.quarkus.strimzi.quickstart;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class PeopleManager {

    private final Logger log = Logger.getLogger(PeopleManager.class);

    private final List<Person> list = new CopyOnWriteArrayList<>();

    @Incoming("people-in")
    public void consume(Person person) {
        list.add(person);
    }

    public List<Person> getPeople() {
        log.info("Returning people " + list);
        return list;
    }
}
