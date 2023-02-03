package org.acme.quartz;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class TaskBean {

    @Transactional
    @Scheduled(every = "10s", identity = "task-job")
    void schedule() {
        Task task = new Task();
        task.persist();
    }
}
