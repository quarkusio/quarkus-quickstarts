# Getting Started Guides

This repository contains a set of examples about the Quarkus framework.

* [Getting Started](./getting-started): Application creation, Rest endpoint, Dependency Injection, Test, Packaging
* [Getting Started - Async](./getting-started-async): Illustrate how to use `CompletionStage` to handle asynchronous actions
* [Getting Started - Native](./getting-started-native): Packaging of the application into a native executable
* [Getting Started - Kubernetes](./getting-started-kubernetes): Deployment of the application to Kubernetes and/or OpenShift
* [Application Configuration](./application-configuration): How to configure your application
* [Hibernate ORM and RESTEasy](./hibernate-resteasy): Exposing a CRUD service over REST using Hibernate ORM to connect to a PostgreSQL database
* [Scheduling periodic tasks](./scheduling-periodic-tasks): How to schedule periodic jobs
* [Using Web Sockets](./using-websockets): Demonstrate how to use web sockets and serve static assets
* [Startup and Shutdown actions](./application-lifecycle-events): Explains how to execute code when the application starts and stops
* [Validation and JSON](./input-validation): How to consume and produce JSON payloads and how to validate the user input with Bean Validation
* [REST Client](./rest-client): How to use MicroProfile's REST Client
* [Infinispan Client](./infinispan-client): How to use Infinispan Client. Covers creating caches and simple get/put

There is documentation published at <http://10.0.144.40/nfs/quarkus/index.html> (docs' [sources are here](https://github.com/jbossas/quarkus/tree/master/docs/src/main/asciidoc)).

## Prerequisites

* [Maven 3.5+](https://maven.apache.org/install.html)
* [Java - OpenJDK 1.8+](https://adoptopenjdk.net/)
