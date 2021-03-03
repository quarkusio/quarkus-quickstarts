How to use _bare_ Kafka clients in Quarkus
============================================

This project illustrates how to use _bare_ Apache Kafka clients and Vert.x Kafka clients in a Quarkus application.

## Kafka cluster

First, you need a Kafka cluster. You can follow the instructions from the [Apache Kafka web site](https://kafka.apache.org/quickstart) or run `docker-compose up` if you have docker installed on your machine.

## Start the application

Start the application in _dev mode_ using: 

```bash
mvn quarkus:dev
```

## Post a Kafka record

```bash 
# post a record using the bare Kafka client
> http POST :8080/kafka key==bare value==value
# post a record using the Vert.x Kafka client
> http POST :8080/vertx-kafka key==bare value==value
```

## Retrieve the last record

```bash
# retrieve a record using the bare Kafka client
> http :8080/kafka 

# retrieve a record using the Vert.x Kafka client
> http :8080/vertx-kafka
```

## List existing topics

```bash
# retrieve the list of topics using the bare Kafka client
> http :8080/kafka/topics

# retrieve the list of topics using the Vert.x Kafka client
> http :8080/vertx-kafka/topics
```

## Anatomy

The `org.acme.kafka.KafkaProviders` and `org.acme.kafka.VertxKafkaProviders` produces the Kafka consumers, produces and admin for the _bare_ API and Vert.x API.
The `org.acme.kafka.KafkaEndpoint` and `org.acme.kafka.VertxKafkaEndpoint` demonstrates how these clients can be used.

Note that the configuration is retrieved using:

```java
@Inject
@Named("default-kafka-broker")
Map<String, Object> config;
```

This config contains all the `kafka.` properties from the application configuration.

## Running in native

You can compile the application into a native binary using:

`mvn clean install -Pnative`

and run with:

`./target/kafka-bare-quickstart-1.0-SNAPSHOT-runner` 
