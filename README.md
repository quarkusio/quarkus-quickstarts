# Getting Started Guides

This repository contains a set of examples about the Quarkus framework.

See [CONTRIBUTING](CONTRIBUTING.md) for how to build these examples:

* [Getting Started](./getting-started): Application creation, Rest endpoint, Dependency Injection, Test, Packaging
* [Getting Started - Async](./getting-started-async): Illustrate how to use `CompletionStage` to handle asynchronous actions
* [Getting Started - Knative](./getting-started-knative): Deployment of the Knative service to Kubernetes and/or OpenShift
* [Getting Started - Testing](./getting-started-testing): How to test your application
* [Application Configuration](./config-quickstart): How to configure your application
* [Startup and Shutdown actions](./lifecycle-quickstart): Explains how to execute code when the application starts and stops


* [AMQP](./amqp-quickstart): How to interact with AMQP using MicroProfile reactive messaging
* [Amazon DynamoDB](./dynamodb-quickstart): How to connect to an Amazon DynamoDB datastore
* [Hibernate ORM and RESTEasy](./hibernate-orm-quickstart): Exposing a CRUD service over REST using Hibernate ORM to connect to a PostgreSQL database
* [Hibernate ORM with Panache and RESTEasy](./hibernate-orm-panache-quickstart): Exposing a CRUD service over REST using Panache to connect to a PostgreSQL database
* [Hibernate Search + Elasticsearch](./hibernate-search-elasticsearch-quickstart): Index your Hibernate entities in Elasticsearch to get full text search
* [Infinispan Client](./infinispan-client-quickstart): How to use Infinispan Client. Covers creating caches and simple get/put
* [Artemis JMS](./jms-quickstart): How to use the Artemis JMS extension
* [Kafka](./kafka-quickstart): Use MicroProfile Reactive Messaging to interact with Apache Kafka
* [Kafka Streams](./kafka-streams-quickstart): Use the Apache Kafka Streams API to implement stream processing applications based on Apache Kafka
* [Kogito](./kogito-quickstart): How to use Kogito for business process automation with Drools and jBPM
* [MicroProfile Fault Tolerance](./microprofile-fault-tolerance-quickstart): How to use MicroProfile Fault Tolerance
* [MicroProfile Health](./microprofile-health-quickstart): How to use MicroProfile Health
* [MicroProfile Metrics](./microprofile-metrics-quickstart): How to use MicroProfile Metrics
* [MongoDB](./mongodb-quickstart): How to connect to a MongoDB datastore
* [MongoDB with Panache](./mongodb-panache-quickstart): Simplify your MongoDB applications with Panache
* [MicroPofile Messaging MQTT](./mqtt-quickstart): How to interact with MQTT using MicroProfile reactive messaging
* [Neo4j](./neo4j-quickstart): How to connect to a Neo4j graph datastore
* [OpenAPI and Swagger UI](./openapi-swaggerui-quickstart): Use OpenAPI and Swagger UI to expose your REST API and test your REST services
* [OpenTracing and Jaeger](./opentracing-quickstart): How to use MicroProfile OpenTracing and Jaeger to monitor application performances
* [Reactive Routes](./reactive-routes-quickstart): How to use Reactive Routes in Quarkus
* [REST Client](./rest-client-quickstart): How to use MicroProfile's REST Client
* [REST Client Multipart](./rest-client-multipart-quickstart): Handle multipart with MicroProfile's REST Client
* [JSON REST services](./rest-json-quickstart): How to write JSON REST services
* [Scheduling periodic tasks](./scheduler-quickstart): How to schedule periodic jobs
* [Security with Users stored in a Database](./security-jdbc-quickstart): Store your users in a database and secure your application
* [Security with MicroProfile JWT](./security-jwt-quickstart): How to use MicroProfile JWT RBAC
* [Security with OAuth2 opaque tokens](./security-oauth2-quickstart): How to use our security layer with OAuth2 opaque tokens
* [Security with OpenId Connect](./security-openid-connect-quickstart): How to use OpenId Connect and [Keycloak](https://www.keycloak.org)
* [Spring DI compatibility layer](./spring-di-quickstart): How to use our Spring Dependency Injection compatibility layer
* [Spring Data extension](./spring-data-jpa-quickstart): How to the Quarkus extension for the Spring Data API
* [Spring Web extension](./spring-web-quickstart): How to the Quarkus extension for the Spring Web API
* [Using STM](./software-transactional-memory-quickstart): How to use the Narayana Software Transactional Memory extension
* [Apache Tika](./tika-quickstart): Extract metadata and content from your documents
* [Validation with Hibernate Validator](./validation-quickstart): How to use Hibernate Validator/Bean Validation in your REST services
* [Using Vert.x](./vertx-quickstart): How to use the Vert.x extension
* [Using Web Sockets](./websockets-quickstart): Demonstrate how to use web sockets and serve static assets

There is documentation published at <https://quarkus.io> (docs' [sources are here](https://github.com/quarkusio/quarkus/tree/master/docs/src/main/asciidoc)).

## Requirements

To compile and run these demos you will need:

- JDK 8 or 11+
- GraalVM

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image) for help setting up your environment.
