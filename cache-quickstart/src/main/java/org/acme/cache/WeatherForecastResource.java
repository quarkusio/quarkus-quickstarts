package org.acme.cache;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.jboss.resteasy.reactive.RestQuery;

@Path("/weather")
public class WeatherForecastResource {

    @Inject
    WeatherForecastService service;

    @GET
    public WeatherForecast getForecast(@RestQuery String city, @RestQuery long daysInFuture) {
        long executionStart = System.currentTimeMillis();
        List<String> dailyForecasts = Arrays.asList(
                service.getDailyForecast(LocalDate.now().plusDays(daysInFuture), city),
                service.getDailyForecast(LocalDate.now().plusDays(daysInFuture + 1L), city),
                service.getDailyForecast(LocalDate.now().plusDays(daysInFuture + 2L), city));
        long executionEnd = System.currentTimeMillis();
        return new WeatherForecast(dailyForecasts, executionEnd - executionStart);
    }
}
