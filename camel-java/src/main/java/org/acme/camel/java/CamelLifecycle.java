package org.acme.camel.java;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import io.quarkus.camel.core.runtime.CamelRuntime;
import io.quarkus.camel.core.runtime.InitializedEvent;
import io.quarkus.camel.core.runtime.InitializingEvent;
import io.quarkus.camel.core.runtime.StartedEvent;
import io.quarkus.camel.core.runtime.StartingEvent;
import io.quarkus.camel.core.runtime.StoppedEvent;
import io.quarkus.camel.core.runtime.StoppingEvent;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultUuidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class CamelLifecycle {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    CamelRuntime runtime;

    @Inject
    CamelContext context;

    public void initializing(@Observes InitializingEvent event) {
        log.info("Initializing Camel...");
        runtime.getContext().setUuidGenerator(new DefaultUuidGenerator());
    }

    public void initialized(@Observes InitializedEvent event) {
        log.info("Initialized Camel...");
        log.info("Context: {}", context);
    }

    public void starting(@Observes StartingEvent event) {
        log.info("Starting Camel...");
    }

    public void started(@Observes StartedEvent event) {
        log.info("Started Camel...");
    }

    public void stopping(@Observes StoppingEvent event) {
        log.info("Stopping Camel...");
    }

    public void stopped(@Observes StoppedEvent event) {
        log.info("Stopped Camel...");
    }

}
