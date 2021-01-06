package io.quarkus.strimzi.quickstart;

import io.quarkus.kafka.client.serialization.JsonbDeserializer;

public class PersonDeserializer extends JsonbDeserializer<Person> {
    public PersonDeserializer() {
        super(Person.class);
    }
}
