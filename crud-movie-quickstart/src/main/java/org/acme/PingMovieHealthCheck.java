package org.acme;

import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
public class PingMovieHealthCheck implements HealthCheck {
	@Inject
	MovieResource resource;

	@Override
	public HealthCheckResponse call() {
		var response = resource.hello();

		return HealthCheckResponse.named("Ping Movie REST Endpoint")
			.withData("Response", response)
			.up()
			.build();
	}
}