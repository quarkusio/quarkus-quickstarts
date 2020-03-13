package org.acme.microprofile.faulttolerance;

import static io.restassured.RestAssured.get;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringContains.containsString;

import org.junit.jupiter.api.Test;

public abstract class BaseTest {

	@Test
	public void testAvailability() {
		get("/coffee/1/availability").then()
				.statusCode(200).body(is("20"));
		get("/coffee/1/availability").then()
				.statusCode(200).body(is("20"));
		get("/coffee/1/availability").then()
				.statusCode(500).body(is("RuntimeException: Service failed."));
		get("/coffee/1/availability").then()
				.statusCode(500).body(is("RuntimeException: Service failed."));
		get("/coffee/1/availability").then()
				.statusCode(500).body(containsString("CircuitBreakerOpenException"));
	}

	@Test
	public void testRecommendations() {
		get("/coffee/2/recommendations").then()
				.statusCode(200)
				.body("id", hasItem(1),
						"countryOfOrigin", hasItem("Colombia"));
	}
}
