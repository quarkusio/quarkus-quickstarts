package org.acme;

import io.quarkus.funqy.Funq;
import io.quarkus.funqy.gcp.functions.event.PubsubMessage;
import io.quarkus.funqy.gcp.functions.event.StorageEvent;
import org.acme.service.GreetingService;

import javax.inject.Inject;

public class GreetingFunctions {

    @Inject GreetingService service;

    @Funq
    public void helloPubSubWorld(PubsubMessage pubSubEvent) {
        String message = service.hello(pubSubEvent.data);
        System.out.println(pubSubEvent.messageId + " - " + message);
    }

    @Funq
    public void helloGCSWorld(StorageEvent storageEvent) {
        String message = service.hello("world");
        System.out.println(storageEvent.name + " - " + message);
    }

}