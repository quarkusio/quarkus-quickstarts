package org.acme.lifecycle;

import io.quarkus.runtime.Startup;
import lombok.Getter;

import javax.enterprise.context.ApplicationScoped;

@Startup
@ApplicationScoped
public class EagerAppBean {

    @Getter
    private final String name;

    EagerAppBean(NameGenerator generator) {
        this.name = generator.createName();
    }
}
