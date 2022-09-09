package org.acme.infinispan.client;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import io.quarkus.infinispan.client.Remote;
import io.quarkus.logging.Log;
import org.infinispan.client.hotrod.RemoteCache;

import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class InfinispanClientApp {

    @Inject
    @Remote("mycache")
    RemoteCache<String, String> cache;

    void onStart(@Observes StartupEvent ev) {
        Log.info("Get cache named mycache and add put a key/value");
        cache.put("hello", "Hello World, Infinispan is up!");
    }
}
