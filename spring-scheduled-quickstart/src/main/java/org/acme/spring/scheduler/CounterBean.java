package org.acme.spring.scheduler;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.atomic.AtomicInteger;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CounterBean {

    private AtomicInteger counter = new AtomicInteger();

    public int get() {
        return counter.get();
    }

    @Scheduled(cron="0/1 * * * * ?")
    void cronJob() {
        counter.incrementAndGet();
        System.out.println("Cron expression hardcoded");
    }

    @Scheduled(cron = "{cron.expr}")
    void cronJobWithExpressionInConfig() {
        counter.incrementAndGet();
        System.out.println("Cron expression configured in application.properties");
    }

    @Scheduled(fixedRate = 1000)
    void jobAtFixedRate() {
        counter.incrementAndGet();
        System.out.println("Fixed Rate expression");
    }

    @Scheduled(fixedRateString = "${fixedRate.expr}")
    void jobAtFixedRateInConfig() {
        counter.incrementAndGet();
        System.out.println("Fixed Rate expression configured in application.properties");
    }
}