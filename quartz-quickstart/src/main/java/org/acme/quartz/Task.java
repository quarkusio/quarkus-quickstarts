package org.acme.quartz;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "TASKS")
public class Task extends PanacheEntity {
    public Instant createdAt;

    public Task() {
        createdAt = Instant.now();
    }

    public Task(Instant time) {
        this.createdAt = time;
    }
}
