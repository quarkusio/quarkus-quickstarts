# Stork Automatic Consul Registration Quickstart

Quarkus guide: https://quarkus.io/guides/stork-registration

This quickstart demonstrates **zero-configuration automatic service registration** with Consul using Stork. By simply adding the `stork-service-registration-consul` dependency to the classpath, Quarkus automatically registers the application in Consul on startup — no application code or configuration properties required.

See also the [stork-programmatic-custom-registration-quickstart](../stork-programmatic-custom-registration-quickstart) for the programmatic/custom registration approach.

## How it works

The `application.properties` file contains no active properties. The 6 commented "cases" serve as inline documentation, progressively showing what *could* be configured (explicit IP, multiple services, health checks, disabling registration, etc.), but the quickstart works without any of them. The automatic registration works out of the box with sensible defaults (application name, detected IP, HTTP port).

## Requirements

- Java 17+
- Docker (required by Testcontainers to run Consul during tests)

## Running the tests

Tests use Testcontainers to start a Consul instance automatically, so no manual setup is needed:

```bash
./mvnw test
```

The test verifies that the service appears in Consul's registry (`/v1/agent/service/red-service`) after Quarkus starts.

## Native mode

```bash
./mvnw clean verify -Pnative
```

## Consul reference

Start a Consul instance manually (for development):

```shell
docker run \
    -d \
    -p 8500:8500 \
    -p 8600:8600/udp \
    consul agent -server -ui -node=server-1 -bootstrap-expect=1 -client=0.0.0.0
```

Register a service:

```bash
curl -X PUT -d '{"ID": "red", "Name": "red-service", "Address": "localhost", "Port": 9000, "Tags": ["color"]}' http://127.0.0.1:8500/v1/agent/service/register
```

Deregister a service:

```bash
curl -X PUT http://127.0.0.1:8500/v1/agent/service/deregister/red
```

Query a service:

```bash
curl -X GET http://127.0.0.1:8500/v1/agent/service/red
```