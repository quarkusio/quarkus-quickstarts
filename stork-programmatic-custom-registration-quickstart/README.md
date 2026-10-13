Quarkus guide: https://quarkus.io/guides/stork-manual-service-registration

# Stork Programmatic Custom Registration Quickstart

This quickstart demonstrates how to extend SmallRye Stork's service registration by implementing a custom `ServiceRegistrar` 
and programmatically registering service instances at application startup.

The custom registrar stores registered instances in memory.

## Architecture

The project implements two Stork SPI interfaces:

- **`CustomServiceRegistrarProvider`** — Factory annotated with `@ServiceRegistrarType("custom")` that creates `CustomServiceRegistrar` instances.
- **`CustomServiceRegistrar`** — Holds registered service instances in a `ConcurrentHashMap`. Receives host/port configuration from `CustomRegistrarConfiguration` (generated at build time from `@ServiceRegistrarAttribute` annotations).
- **`Registration`** — CDI bean that observes `StartupEvent` and programmatically registers a service instance via `Stork.getInstance().getService("my-service").registerInstance(...)`.

## Configuration

The custom registrar is configured in `application.properties`:

```properties
quarkus.stork.my-service.service-registrar.type=custom
quarkus.stork.my-service.service-registrar.host=localhost
```

## Running the application in dev mode

```bash
./mvnw quarkus:dev
```

## Running tests

```bash
./mvnw test
```

## Building a native executable

```bash
./mvnw verify -Pnative
```