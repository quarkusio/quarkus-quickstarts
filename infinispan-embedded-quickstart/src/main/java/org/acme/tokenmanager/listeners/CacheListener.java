package org.acme.tokenmanager.listeners;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;

import javax.enterprise.event.Observes;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

@Listener
public class CacheListener {

    private final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @CacheEntryCreated
    public void entryCreated(@Observes CacheEntryCreatedEvent event) {
        if (event.getValue() != null) {
            log.info("Cache entry added: [" + event.getKey() + " " + event.getValue().toString() + "]");
        }
    }

    @CacheEntryModified
    public void entryModified(@Observes CacheEntryModifiedEvent event) {
        log.info("Cache entry modified: [" + event.getKey() + " " + event.getValue().toString() + "]");
    }

    @CacheEntryRemoved
    public void entryRemoved(@Observes CacheEntryRemovedEvent event) {
        log.info("entry " + event.getKey() + " removed from the cache");
    }

}