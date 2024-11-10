package org.acme.kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jboss.resteasy.reactive.RestQuery;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@Path("/kafka")
@ApplicationScoped
public class KafkaEndpoint {

    public static final String TOPIC = "hello";

    @Inject
    KafkaConsumer<String, String> consumer;

    @Inject
    KafkaProducer<String, String> producer;

    @Inject
    AdminClient admin;

    volatile boolean done = false;
    volatile String last;

    public void initialize(@Observes StartupEvent ev) {
        consumer.subscribe(Collections.singleton(TOPIC));
        new Thread(() -> {
            while (! done) {
                final ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));

                consumerRecords.forEach(record -> {
                    System.out.printf("Polled Record:(%s, %s, %d, %d)\n",
                            record.key(), record.value(),
                            record.partition(), record.offset());
                    last = record.key() + "-" + record.value();
                });
            }
            consumer.close();
        }).start();
    }


    public void terminate(@Observes ShutdownEvent ev) {
        done = true;
        producer.close();
        admin.close();
    }


    @Path("/topics")
    @GET
    public Set<String> getTopics() throws InterruptedException, ExecutionException, TimeoutException {
        return admin.listTopics()
                .names().get(5, TimeUnit.SECONDS);
    }

    @POST
    public long post(@RestQuery String key, @RestQuery String value)
            throws InterruptedException, ExecutionException, TimeoutException {
        return producer.send(new ProducerRecord<>(TOPIC, key, value)).get(5, TimeUnit.SECONDS)
                .offset();
    }

    @GET
    public String getLast() {
        return last;
    }

}
