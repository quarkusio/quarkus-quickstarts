package org.acme.microprofile.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Default;

import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.health.Readiness;

import io.quarkus.logging.Log;
import io.smallrye.health.event.SmallRyeHealthStatusChangeEvent;

@ApplicationScoped
public class HealthObserver {

    public void observeHealthChange(@Observes @Default SmallRyeHealthStatusChangeEvent event) {
        Log.infof("Health status changed to %s", event.health().getStatus());
    }

    public void observeReadinessChange(@Observes @Readiness SmallRyeHealthStatusChangeEvent event) {
        Log.infof("Readiness status changed to %s", event.health().getStatus());
    }

    public void observeLivenessChange(@Observes @Liveness SmallRyeHealthStatusChangeEvent event) {
        Log.infof("Liveness status changed to %s", event.health().getStatus());
    }
}
