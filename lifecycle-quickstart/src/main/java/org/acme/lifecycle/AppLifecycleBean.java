package org.acme.lifecycle;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import lombok.extern.jbosslog.JBossLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@JBossLog
@ApplicationScoped
public class AppLifecycleBean {

    /**
     * Inject a bean used in the callbacks.
     */
    @Inject
    MyOtherBean bean;

    @Inject
    EagerAppBean eagerAppBean;

    void onStart(@Observes StartupEvent ev) {
        log.infof("The application is starting...%s", bean.hello() + " " + eagerAppBean.getName());
    }

    void onStop(@Observes ShutdownEvent ev) {
        log.infof("The application is stopping...%s", bean.bye() + " " + eagerAppBean.getName());
    }

}
