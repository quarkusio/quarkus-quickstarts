# Quarkus quickstart example for OpenTelemetry

This example provides a simple Quarkus application instrumented with OpenTelemetry, and allows telemetry data to be seen in a local Grafana LGTM DevService instance.

For detailed instructions the [Quarkus OpenTelemetry guide](https://quarkus.io/guides/opentelemetry) is available. 

There are signal specific guides for:
* [Tracing](https://quarkus.io/guides/opentelemetry-tracing)
* [Metrics](https://quarkus.io/guides/opentelemetry-metrics)
* [Logs](https://quarkus.io/guides/opentelemetry-logging)

## See telemetry

The project includes the Grafana LGTM DevService for telemetry visualization. This is provided by the `quarkus-observability-devservices-lgtm` dependency.

Usage and configuration are explained in the [Grafana LGTM guide](https://quarkus.io/guides/observability-devservices-lgtm).

