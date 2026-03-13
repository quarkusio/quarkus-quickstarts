package org.acme.micrometer;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Header;

/**
 * Test class for TemperatureResource using REST-assured.
 */
@QuarkusTest
public class TemperatureResourceTest {

	@BeforeEach
	void resetTemperatures() {
		get("/temperature/reset").then().assertThat().statusCode(200);
	}

	@Test
	void testAddTemperature() {
		when().get("/temperature/add/20.5").then().statusCode(200)
				.body(containsString("Temperature added: 20.5"));
		when().get("/temperature/add/22.3").then().statusCode(200)
				.body(containsString("Temperature added: 22.3"));
	}

	@Test
	void testAverageTemperature() {
		// Add temperatures first
		get("/temperature/add/20.5");
		get("/temperature/add/22.3");
		get("/temperature/add/25.1");

		// Check the average calculation
		when().get("/temperature/average").then().statusCode(200)
				.body(containsString("Average temperature: 22.633333333333333 Celsius"));
	}

	@Test
	void testMaxTemperature() {
		// Add temperatures first
		get("/temperature/add/20.5");
		get("/temperature/add/22.3");
		get("/temperature/add/25.1");

		// Check the maximum temperature calculation
		when().get("/temperature/max").then().statusCode(200)
				.body(containsString("Maximum temperature: 25.1 Celsius"));
	}

	@Test
	void testMinTemperature() {
		// Add temperatures first
		get("/temperature/add/20.5");
		get("/temperature/add/22.3");
		get("/temperature/add/25.1");

		// Check the minimum temperature calculation
		when().get("/temperature/min").then().statusCode(200)
				.body(containsString("Minimum temperature: 20.5 Celsius"));
	}

	@Test
	void testTemperatureMetrics() {
		// Trigger endpoints to populate metrics
		get("/temperature/add/20.5");
		get("/temperature/add/22.3");
		get("/temperature/add/25.1");
		get("/temperature/average");
		get("/temperature/max");
		get("/temperature/min");

		// Check metrics for recorded operations
		when().get("/q/metrics").then().statusCode(200)
				.body(containsString("temperature_readings_sum"))
				.body(containsString("temperature_readings_count 3.0"))
				.body(containsString("temperature_calculation_average_seconds_count 1.0"))
				.body(containsString("temperature_calculation_max_seconds_count 1.0"))
				.body(containsString("temperature_calculation_min_seconds_count 1.0"));
	}
}
