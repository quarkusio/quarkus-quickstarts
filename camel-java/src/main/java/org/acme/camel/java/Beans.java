package org.acme.camel.java;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.apache.camel.component.timer.TimerComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class Beans {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Produces @Named("timer")
    public TimerComponent timer() {
        log.info("Creating timer component from CDI");
        return new TimerComponent();
    }

}
