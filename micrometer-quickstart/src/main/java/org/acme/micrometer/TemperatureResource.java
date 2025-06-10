package org.acme.micrometer;

import java.util.LinkedList;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PathParam;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.DistributionSummary;

/**
 * REST API for monitoring and recording temperature readings.
 */
@Path("/temperature")
@Produces("text/plain")
public class TemperatureResource {

	private final MeterRegistry registry;
	private LinkedList<Double> temperatures = new LinkedList<>();

	DistributionSummary temperatureSummary;

	/**
	 * Constructs a TemperatureResource.
	 * Initializes the monitoring tools for temperatures.
	 *
	 * @param registry MeterRegistry for recording metrics.
	 */
	public TemperatureResource(MeterRegistry registry) {
		this.registry = registry;
		// Summary to record temperature readings and their distribution.
		temperatureSummary = registry.summary("temperature.readings", Tags.of("unit", "celsius"));
		// Gauge to monitor the size of the temperature list in real-time.
		registry.gaugeCollectionSize("temperature.list.size", Tags.empty(), temperatures);
	}

	/**
	 * Adds a temperature reading to the recorded list and updates the summary metric.
	 *
	 * @param temp The temperature to add.
	 * @return A message confirming the addition of the temperature.
	 */
	@GET
	@Path("/add/{temp}")
	public String addTemperature(@PathParam("temp") double temp) {
		temperatures.add(temp);
		temperatureSummary.record(temp);
		return "Temperature added: " + temp;
	}

	/**
	 * Calculates the average of all recorded temperatures.
	 * Uses a timer to measure the time taken to perform this calculation.
	 *
	 * @return The average temperature or a message if no temperatures are recorded.
	 */
	@GET
	@Path("/average")
	public String calculateAverage() {
		Timer timer = registry.timer("temperature.calculation.average");
		return timer.record(() -> {
			if (temperatures.isEmpty()) {
				return "No temperatures recorded.";
			}
			double sum = 0;
			for (Double t : temperatures) {
				sum += t;
			}
			double average = sum / temperatures.size();
			return "Average temperature: " + average + " Celsius";
		});
	}

	/**
	 * Finds the maximum temperature from the recorded list.
	 * Measurement is timed for performance analysis.
	 *
	 * @return The maximum temperature or a message if no temperatures are recorded.
	 */
	@GET
	@Path("/max")
	public String findMaxTemperature() {
		Timer timer = registry.timer("temperature.calculation.max");
		return timer.record(() -> {
			return temperatures.stream()
					.max(Double::compare)
					.map(maxTemp -> "Maximum temperature: " + maxTemp + " Celsius")
					.orElse("No temperatures recorded.");
		});
	}

	/**
	 * Finds the minimum temperature from the recorded list.
	 * Measurement is timed for performance analysis.
	 *
	 * @return The minimum temperature or a message if no temperatures are recorded.
	 */
	@GET
	@Path("/min")
	public String findMinTemperature() {
		Timer timer = registry.timer("temperature.calculation.min");
		return timer.record(() -> {
			return temperatures.stream()
					.min(Double::compare)
					.map(minTemp -> "Minimum temperature: " + minTemp + " Celsius")
					.orElse("No temperatures recorded.");
		});
	}

	// New endpoint to reset the temperatures list
	@GET
	@Path("/reset")
	public String resetTemperatures() {
		temperatures.clear();
		return "Temperatures reset successfully.";
	}
}
