package org.acme.scheduling;

import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.shamrock.scheduler.api.Scheduled;

@ApplicationScoped
public class CounterBean {

    private AtomicInteger counter = new AtomicInteger();

    public int get() {
        return counter.get();
    }

    @Scheduled(every="10s")
    void increment() {
        counter.incrementAndGet();
    }
    
}