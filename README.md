# QuickStarts for Getting Started Guides

This repository contains a set of Quickstarts for the Quarkus framework. Each of them have a Getting started guide.

## Requirements

To compile and run these demos you will need:

- JDK 17+
- Mandrel or GraalVM

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image) for help setting up your environment.

## Use alternative platforms

These quickstart by default currently uses the Quarkus core BOM.

If you want to use an alternative BOM when building the quickstart you can override the `quarkus.platform.*` properties. The following example shows how to set `quarkus.platform.artifact-id` to use the universe-bom.

```
mvn -Dquarkus.platform.artifact-id=quarkus-universe-bom clean install
```

## Contributions

See [CONTRIBUTING](CONTRIBUTING.md) for how to build these examples.

## Quick Start list

* [Getting Started](./getting-started): Application creation, Rest endpoint, Dependency Injection, Test, Packaging
* [Getting Started - Reactive](./getting-started-reactive): Getting started with Reactive and Quarkus
* [Getting Started - Async](./getting-started-async): Illustrate how to use `Uni` to handle asynchronous actions
* [Getting Started - Knative](./getting-started-knative): Deployment of the Knative service to Kubernetes and/or OpenShift
* [Getting Started - Testing](./getting-started-testing): How to test your application
* [Application Configuration](./config-quickstart): How to configure your application
* [Startup and Shutdown actions](./lifecycle-quickstart): Explains how to execute code when the application starts and stops
* [Getting Started - Reactive CRUD](./getting-started-reactive-crud): Reactive REST API with a (reactive) database backend


* [AMQP](./amqp-quickstart): How to interact with AMQP using MicroProfile reactive messaging
* [Amazon DynamoDB](./amazon-dynamodb-quickstart): How to connect to an Amazon DynamoDB datastore
* [Amazon KMS](./amazon-kms-quickstart): How to connect to an Amazon KMS (Key Management Service)
* [Amazon SES](./amazon-ses-quickstart): How to connect to an Amazon SES (Simple Email Service)
* [Amazon S3](./amazon-s3-quickstart): How to connect to an Amazon S3 cloud storage
* [Amazon SNS](./amazon-sns-quickstart): How to connect to an Amazon SNS (Simple Messaging Service)
* [Amazon SQS](./amazon-sqs-quickstart): How to connect to an Amazon SQS (Simple Queue Service)
* [Amazon SQS Connector](./amazon-sqs-connector-quickstart): How to connect to an Amazon SQS (Simple Queue Service) using the Reactive Messaging connector
* [Amazon SSM](./amazon-ssm-quickstart): How to connect to Amazon SSM (Simple Systems Manager)
* [AWT Graphics, ImageIO](./awt-graphics-rest-quickstart): How to create a service that watermarks images and draws text
* [Hibernate Reactive Panache and RESTEasy Reactive](./hibernate-reactive-quickstart): Exposing a CRUD service over REST using Hibernate Reactive and Panache to connect to a PostgreSQL database
* [Hibernate Reactive and RESTEasy](./hibernate-reactive-quickstart): Exposing a CRUD service over REST using Hibernate Reactive to connect to a PostgreSQL database
* [Hibernate Reactive and Vert.x Web](./hibernate-reactive-routes-quickstart): Exposing a CRUD service with Reactive Routes using Hibernate Reactive to connect to a PostgreSQL database
* [Hibernate ORM and RESTEasy](./hibernate-orm-quickstart): Exposing a CRUD service over REST using Hibernate ORM to connect to a PostgreSQL database
* [Hibernate ORM with Panache and RESTEasy](./hibernate-orm-panache-quickstart): Exposing a CRUD service over REST using Panache to connect to a PostgreSQL database
* [Hibernate ORM with Panache and RESTEasy in Kotlin](./hibernate-orm-panache-kotlin-quickstart): Exposing a CRUD service over REST using Panache and kotlin to connect to a PostgreSQL database
* [Hibernate ORM REST Data with Panache](./hibernate-orm-rest-data-panache-quickstart): Automatically generate the CRUD endpoints for your entities and repositories using Hibernate ORM with Panache.
* [Hibernate ORM Multitenancy Database](./hibernate-orm-multi-tenancy-database-quickstart): Multitenant CRUD service over REST using Hibernate ORM to connect to multiple PostgreSQL databases (database approach)
* [Hibernate ORM Multitenancy Schema](./hibernate-orm-multi-tenancy-schema-quickstart): Multitenant CRUD service over REST using Hibernate ORM to connect to a PostgreSQL database (schema approach)
* [Hibernate Search + Elasticsearch](./hibernate-search-orm-elasticsearch-quickstart): Index your Hibernate entities in Elasticsearch to get full text search
* [Blazebit Persistency](./blazebit-quickstart): How to use Blazebit Persistency. Exposes a REST service to load and store data in PostgreSQL
* [Infinispan Client](./infinispan-client-quickstart): How to use Infinispan Client. Covers creating caches and simple get/put
* [Artemis JMS](./jms-quickstart): How to use the Artemis JMS extension
* [Kafka](./kafka-quickstart): Use MicroProfile Reactive Messaging to interact with Apache Kafka
* [Kafka and Hibernate with Panache](./kafka-panache-quickstart): Shows how to combine Kafka and (_classic_) Hibernate with Panache
* [Kafka and Hibernate Reactive with Panache](./kafka-panache-reactive-quickstart): Shows how to combine Kafka and Hibernate Reactive with Panache
* [Kafka Streams](./kafka-streams-quickstart): Use the Apache Kafka Streams API to implement stream processing applications based on Apache Kafka
* [Bare Kafka](./kafka-bare-quickstart): How to use the Apache Kafka and Kafka Vert.x clients in Quarkus
* [Liquibase](./liquibase-quickstart): How to use Liquibase to manage you schema migrations
* [Liquibase MongoDB](./liquibase-mongodb-quickstart): How to use Liquibase MongoDB extension to manage you MongoDB migrations
* [Micrometer](./micrometer-quickstart): How to use Micrometer to gather metrics
* [MicroProfile Fault Tolerance](./microprofile-fault-tolerance-quickstart): How to use MicroProfile Fault Tolerance
* [MicroProfile GraphQL](./microprofile-graphql-quickstart): How to use MicroProfile GraphQL
* [MicroProfile GraphQL Client](./microprofile-graphql-client-quickstart): How to use MicroProfile GraphQL Client
* [MicroProfile Health](./microprofile-health-quickstart): How to use MicroProfile Health
* [MicroProfile Metrics](./microprofile-metrics-quickstart): How to use MicroProfile Metrics
* [MongoDB](./mongodb-quickstart): How to connect to a MongoDB datastore
* [MongoDB with Panache](./mongodb-panache-quickstart): Simplify your MongoDB applications with Panache
* [MicroProfile Messaging MQTT](./mqtt-quickstart): How to interact with MQTT using MicroProfile reactive messaging
* [Neo4j](./neo4j-quickstart): How to connect to a Neo4j graph datastore
* [Pulsar](./pulsar-quickstart):  How to interact with Apache Pulsar using MicroProfile reactive messaging
* [OpenAPI and Swagger UI](./openapi-swaggerui-quickstart): Use OpenAPI and Swagger UI to expose your REST API and test your REST services
* [OpenTelemetry](./opentelemetry-quickstart): How to use OpenTelemetry to monitor application performance
* [OptaPlanner](./optaplanner-quickstart): How to use OptaPlanner to optimize business resources
* [Quartz](./quartz-quickstart): How to schedule periodic clustered jobs
* [Qute](./qute-quickstart): How to use the Qute templating engine in Quarkus
* [RabbitMQ](./rabbitmq-quickstart): How to interact with RabbitMQ using MicroProfile reactive messaging
* [Reactive Routes](./reactive-routes-quickstart): How to use Reactive Routes in Quarkus
* [REST Client Reactive](./rest-client-quickstart): How to use MicroProfile's REST Client with Reactive client
* [REST Client](./rest-client-quickstart): How to use MicroProfile's REST Client
* [REST Client Multipart](./rest-client-multipart-quickstart): Handle multipart with MicroProfile's REST Client
* [JSON REST services](./rest-json-quickstart): How to write JSON REST services
* [Scheduling periodic tasks](./scheduler-quickstart): How to schedule periodic jobs
* [Security with Users stored in a Database (JDBC)](./security-jdbc-quickstart): Store your users in a database and secure your application with JDBC
* [Security with Users stored in a Database (JPA)](./security-jpa-quickstart): Store your users in a database and secure your application with JPA
* [Security with Users stored in a Database (JPA Reactive)](./security-jpa-reactive-quickstart): Store your users in a database and secure your application using Hibernate Reactive
* [Security with MicroProfile JWT](./security-jwt-quickstart): How to use MicroProfile JWT RBAC
* [Security with an LDAP realm](./security-ldap-quickstart): How to use an LDAP server to authenticate and authorize your user identities
* [Security with OAuth2 opaque tokens](./security-oauth2-quickstart): How to use our security layer with OAuth2 opaque tokens
* [Security with OpenId Connect](./security-openid-connect-quickstart): How to use OpenId Connect and [Keycloak](https://www.keycloak.org)
* [Security with MicroProfile JWT](./security-jwt-quickstart): How to use MicroProfile JWT RBAC
* [Security with WebAuthn using Hibernate ORM](./security-webauthn-quickstart): Authenticate your users using WebAuthn and Hibernate ORM
* [Security with WebAuthn using Hibernate Reactive](./security-webauthn-reactive-quickstart): Authenticate your users using WebAuthn and Hibernate Reactive
* [Supporting Multi-Tenancy in OpenID Connect Applications](./security-openid-connect-multi-tenancy): How to use OpenId Connect and [Keycloak](https://www.keycloak.org)
* [Spring DI compatibility layer](./spring-di-quickstart): How to use our Spring Dependency Injection compatibility layer
* [Spring Data extension](./spring-data-jpa-quickstart): How to use the Quarkus extension for the Spring Data API
* [Spring Web extension](./spring-web-quickstart): How to use the Quarkus extension for the Spring Web API
* [Spring Security extension](./spring-security-quickstart): How to use the Quarkus extension for the Spring Security API
* [Spring Boot Properties extension](./spring-boot-properties-quickstart): How to use the Quarkus extension for the Spring Boot properties
* [Spring Scheduled extension](./spring-scheduled-quickstart): How to use the Quarkus extension for the Spring Scheduled annotation
* [Spring Data REST extension](./spring-data-rest-quickstart): How to use the Quarkus extension for the Spring Data REST
* [Using STM](./software-transactional-memory-quickstart): How to use the Narayana Software Transactional Memory extension
* [Stork Consul](./stork-quickstart): How to discover and select services with Consul
* [Stork Kubernetes](./stork-kubernetes-quickstart): How to discover and select services with Kubernetes
* [Stork Dns](./stork-dns-quickstart): How to discover and select services with DNS
* [Apache Tika](./tika-quickstart): Extract metadata and content from your documents
* [Validation with Hibernate Validator](./validation-quickstart): How to use Hibernate Validator/Bean Validation in your REST services
* [Using Vert.x](./vertx-quickstart): How to use the Vert.x extension
* [Using Web Sockets](./websockets-quickstart): Demonstrate how to use web sockets and serve static assets
* [Context Propagation](./context-propagation): Demonstrate how to use context propagation in your reactive applications
* [Funqy HTTP](./funqy-quickstarts/funqy-http-quickstart): Funqy functions as HTTP/REST endpoints
* [Funqy AWS Lambda](./funqy-quickstarts/funqy-amazon-lambda-quickstart): Funqy functions as deployed to AWS Lambda
* [Funqy AWS Lambda HTTP](./funqy-quickstarts/funqy-amazon-lambda-http-quickstart): Funqy functions as deployed to AWS Lambda invokable via HTTP/REST
* [Funqy Azure Functions HTTP](./funqy-quickstarts/funqy-azure-functions-quickstart): Funqy functions as deployed to Azure Functions invokable via HTTP/REST
* [Funqy Knative Events](./funqy-quickstarts/funqy-knative-events-quickstart): Funqy functions integration with Knative Events
* [Funqy Google Cloud Functions](./funqy-quickstarts/funqy-google-cloud-functions-quickstart): Funqy functions as deployed to Google Cloud Functions
* [gRPC Plain text Quickstart](./grpc-plain-text-quickstart): How to use Quarkus gRPC extension with plain text communication
* [gRPC TLS Quickstart](./grpc-tls-quickstart): How to use Quarkus gRPC extension with TLS communication
* [Mailer Quickstart](./mailer-quickstart): How to use the Quarkus mailer

* [Using Vert.x Redis Client](./redis-quickstart): Demonstrate how to use Vert.x Redis Client

* [Google Cloud Functions](./google-cloud-functions-quickstart): How to create Google Cloud Functions
* [Google Cloud Functions HTTP](./google-cloud-functions-http-quickstart): How to bind our HTTP layer (JAX-RS, Servlet or Reactive Route) to Google Cloud Functions
* [JTA](./jta-quickstart): How use JTA transactions with HTTP/REST
* [Reactive Messaging with HTTP](./reactive-messaging-http-quickstart): How to consume and produce HTTP messages with Reactive Messaging
* [Reactive Messaging with Web Sockets](./reactive-messaging-websockets-quickstart): How to consume and produce messages via Web Sockets with Reactive Messaging
* [Elasticsearch](./elasticsearch-quickstart): How to use the Elasticsearch REST client and the Java API client


There is documentation published at <https://quarkus.io> (docs' [sources are here](https://github.com/quarkusio/quarkus/tree/main/docs/src/main/asciidoc)).

