Quarkus Kafka Quickstart
========================

This project illustrates how Quarkus applications can interact with Apache Kafka using MicroProfile Reactive Messaging.

## Start the application

The application is composed of two applications communicating through Kafka.
Interactions with Kafka is managed by MicroProfile Reactive Messaging.

They can be started in dev mode using:

```bash
mvn -f producer quarkus:dev
```

and in another terminal:

```bash
mvn -f processor quarkus:dev
```

_NOTE_: Quarkus Dev Services starts a Kafka broker for you automatically. 

Then, open your browser to `http://localhost:8080/quotes.html`.
You can send quote requests and observe received quotes.

## Anatomy

The application is composed of the following components:

#### Producer

The _producer_ application receive requests from the user (via HTTP) and sends data to the Kafka broker.
Two main components compose the application:

* `QuoteProducer` generates uniquely identified quote requests and sends them to the Kafka topic `quote-requests`.
It also consumes the Kafka topic `quotes` and relays received messages to the browser using Server-Sent Events.
* `quotes.html` sends quote requests to the previous endpoint and updates quotes with received prices.

#### Processor

The _processor_ application receives data from Kafka, processes them, and writes the result into the `quotes` Kafka topic.

Two main classes compose the application:

* `QuoteProcessor` consumes quote request id's from the `quote-requests` Kafka topic and responds back to the `quotes` topic with a `Quote` object containing a random price.

The connection to Kafka is configured in the `src/main/resources/application.properties` file.

## Running the application in Docker

To run the application on Docker:

First make sure that both services are packaged:
```bash
mvn package
```

Then launch the Docker compose:
```bash
docker compose up
```

This will create a single-node Kafka cluster and launch both applications.

## Running in native

You can compile the application into a native binary using:

```bash
mvn install -Pnative
```

As you are running in _prod_ mode, you need a Kafka cluster. You can follow the instructions from the [Apache Kafka web site](https://kafka.apache.org/quickstart) or run `docker-compose up` if you have docker installed on your machine.

Then run both applications respectively with:

```bash
./producer/target/kafka-quickstart-producer-1.0.0-SNAPSHOT-runner
```

and in another terminal:

```bash
./processor/target/kafka-quickstart-processor-1.0.0-SNAPSHOT-runner
```
