package org.acme.tokenmanager.config;

import io.quarkus.arc.AlternativePriority;
import org.acme.tokenmanager.controllers.dtos.Entry;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class DefaultCacheProducer {

    public static final Logger log = Logger.getLogger(DefaultCacheProducer.class);

    @ConfigProperty(name = "svc.cache.mode")
    private String cacheModeValue;

    @ConfigProperty(name = "svc.cache.entry.lifespan.hours")
    private long cacheEntryLifespan;

    @Produces
    @DefaultCache
    public Cache<String, Entry> returnCache() {

        Cache<String, Entry> cache = defaultCacheContainer().getCache();
        return cache;
    }

    @Produces
    public Configuration defaultCacheConfiguration() {

        log.info("Configuring default-cache...");

        CacheMode cacheMode = CacheMode.valueOf(cacheModeValue);

        Configuration config = new ConfigurationBuilder()
                .clustering()
                .cacheMode(cacheMode)
                .l1().lifespan(25000L)
                .hash().numOwners(2)
                .expiration()
                .lifespan(cacheEntryLifespan, TimeUnit.HOURS)
                .build();

        return config;
    }

    @Produces
    @AlternativePriority(1)
    public EmbeddedCacheManager defaultCacheContainer() {

        GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
                .clusteredDefault()
                .defaultCacheName("default")
                .serialization()
                .addContextInitializers(new EntriesSerializationContextInitializerImpl())
                .build();

        return new DefaultCacheManager(globalConfig, defaultCacheConfiguration());
    }

}
