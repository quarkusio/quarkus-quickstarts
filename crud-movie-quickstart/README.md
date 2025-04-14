# CRUD Movie Quickstart

This is a simple CRUD application for managing movie rating.
It allows you to create, read, update, and delete movie rating records.

You may find this example overly complicated for such a simple task.
It is intended to represent a more realistic application ready for production.

The application uses the following extensions:

- Quarkus REST (with Jackson)
- Hibernate ORM with Panache
- Hibernate Validator
- PostgreSQL
- SmallRye Health
- SmallRye OpenAPI
- Micrometer / Prometheus
- OpenTelemetry

It uses virtual threads so, you need to run it with Java 21 or later.
