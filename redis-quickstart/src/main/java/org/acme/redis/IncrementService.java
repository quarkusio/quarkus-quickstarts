package org.acme.redis;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.ReactiveKeyCommands;
import io.quarkus.redis.datasource.string.StringCommands;
import io.smallrye.mutiny.Uni;

import java.util.List;

import jakarta.inject.Singleton;

@Singleton
class IncrementService {


    
    private ReactiveKeyCommands<String> keys;
    private StringCommands<String, Integer> counter;

    public IncrementService(RedisDataSource redisDS,  ReactiveRedisDataSource reactiveRedisDS) {
        keys = reactiveRedisDS.key();
        counter = redisDS.string(Integer.class);
    }


    Uni<Void> del(String key) {
        return keys.del(key)
            .replaceWithVoid();
            
    }

    int get(String key) {
        return counter.get(key);
    }

    void set(String key, int value) {
        counter.set(key, value);
    }

    void increment(String key, int incrementBy) {
        counter.incrby(key, incrementBy);
    }

    Uni<List<String>> keys() {
        return keys
                .keys("*");
    }
}

