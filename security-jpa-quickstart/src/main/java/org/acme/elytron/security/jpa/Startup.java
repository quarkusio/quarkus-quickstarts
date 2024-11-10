package org.acme.elytron.security.jpa;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import io.quarkus.runtime.StartupEvent;

@Singleton
public class Startup {
    @Transactional
    public void loadUsers(@Observes StartupEvent evt) {
        // reset and load all test users
        User.deleteAll();
        User.add("admin", "admin", "admin");
        User.add("user", "user", "user");
    }
}
