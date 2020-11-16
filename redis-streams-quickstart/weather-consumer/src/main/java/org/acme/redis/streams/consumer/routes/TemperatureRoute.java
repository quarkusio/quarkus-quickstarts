package org.acme.redis.streams.consumer.routes;

import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.ReactiveRoutes;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import org.acme.redis.streams.consumer.processors.TemperatureProcessor;

import javax.inject.Inject;
import javax.validation.constraints.Min;

@RouteBase(path = "/temperatures", produces = "application/json")
public class TemperatureRoute {

    @Inject
    TemperatureProcessor processor;

    @Route(path = "/", methods = HttpMethod.GET)
    public Multi<String> getAllTemperatures() {
        return ReactiveRoutes.asJsonArray(
                this.processor.getTemperatureAggregates().onItem().transformToMulti(items -> Multi.createFrom().iterable(items))
        );
    }

    @Route(path = "/:id", methods = HttpMethod.GET)
    public Uni<String> getTemperatureByStationId(@Min(1) @Param("id") String id) {
        return this.processor.getTemperatureAggregateByStationId(id);
    }
}
