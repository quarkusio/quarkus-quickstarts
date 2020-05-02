package org.acme.tokenmanager.listeners;

import io.quarkus.runtime.StartupEvent;
import org.infinispan.manager.EmbeddedCacheManager;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

public class CacheListenerRegister {

    @Inject
    EmbeddedCacheManager cacheManager;

    @Inject
    CacheListener cacheListener;

    public void onStartup(@Observes StartupEvent event) {

        cacheManager.addListener(cacheListener);
    }
}
