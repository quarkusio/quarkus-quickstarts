package org.acme.infinispan.client;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import io.quarkus.logging.Log;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class InfinispanClientApp {


    @Inject
    RemoteCacheManager cacheManager;

    void onStart(@Observes StartupEvent ev) {
        Log.info("Get cache named mycache and add put a key/value");
        RemoteCache<Object, Object> cache = cacheManager.getCache("mycache");
        cache.put("hello", "Hello World, Infinispan is up!");
    }
}
