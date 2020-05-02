package org.acme.tokenmanager.services;

import org.acme.tokenmanager.config.DefaultCache;
import org.acme.tokenmanager.controllers.dtos.Entry;
import org.infinispan.Cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

@ApplicationScoped
public class CacheService {

    @Inject
    @DefaultCache
    Cache<String, Entry> cache;

    public String putInCache(Entry entry) {

        String token = UUID.randomUUID().toString();
        cache.put(token, entry);

        return token;
    }

    public Entry getEntry(String token){

        Entry entry = cache.get(token);
        if (entry == null) {
            return new Entry();
        }
        return entry;
    }
}
