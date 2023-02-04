package org.acme.software.transactional.memory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/stm")
@RequestScoped
public class FlightResource {
    ExecutorService executor;

    @ConfigProperty(name = "org.acme.quickstart.stm.threadpool.size")
    int threadPoolSize;;

    @Inject
    FlightServiceFactory factory;

    @PostConstruct
    void postConstruct() {
        executor = Executors.newFixedThreadPool(threadPoolSize);
    }

    @PreDestroy
    void preDestroy() {
        executor.shutdown();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<String> bookingCount() {
        return CompletableFuture.supplyAsync(
                () -> getInfo(factory.getInstance()),
                executor);
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<String> asynBook() {
        return CompletableFuture.supplyAsync(() -> {
            FlightService flightService = factory.getInstance();

            flightService.makeBooking("BA123");

            return getInfo(flightService);
        }, executor);
    }

    @POST
    @Path("sync")
    @Produces(MediaType.TEXT_PLAIN)
    public String book() {
        FlightService flightService = factory.getInstance();

        flightService.makeBooking("BA123");

        return getInfo(flightService);
    }

    private String getInfo(FlightService flightService) {
        return Thread.currentThread().getName()
                + ":  Booking Count=" + flightService.getNumberOfBookings();
    }
}
