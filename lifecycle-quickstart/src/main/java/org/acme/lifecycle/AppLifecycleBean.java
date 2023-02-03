package org.acme.lifecycle;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class AppLifecycleBean {

    private static final Logger LOGGER = LoggerFactory.getLogger("ListenerBean");

    /**
     * Inject a bean used in the callbacks.
     */
    @Inject
    MyOtherBean bean;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...{}", bean.hello());
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping... {}", bean.bye());
    }

}
