package org.acme.lifecycle;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NameGenerator {
    public String createName() {
        return "Quarkus";
    }
}
