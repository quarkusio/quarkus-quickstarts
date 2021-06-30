Quarkus Kafka Quickstart
========================

This project illustrates how you can interact with Apache Kafka using MicroProfile Reactive Messaging.

## Start the application

The application is composed of two microservices. They can be started using: 

```bash
mvn -f producer quarkus:dev
```

and

```bash
mvn -f processor quarkus:dev
```

_NOTE_: Quarkus Dev Services starts a Kafka broker for you automatically. 

Then, open your browser to `http://localhost:8080/quotes.html`.
You can send quote requests and observe received quotes.

## Anatomy

The application is composed of the following microservices:

#### Producer

* `QuoteProducer` generates uniquely identified quote requests and sends them to the Kafka topic `quote-requests`.
It also consumes the Kafka topic `quotes` and relays received messages to the browser using Server-Sent Events.
* `quotes.html` sends quote requests to the previous endpoint and updates quotes with received prices.

#### Processor

* `QuoteProcessor` consumes quote request id's from the `quote-requests` Kafka topic and responds back to the `quotes` topic with a `Quote` object containing a random price.

Interactions with Kafka is managed by MicroProfile Reactive Messaging.
Application configurations are located in `application.properties` files.

## Running the application in Docker

To run the application on Docker:

First make sure that both services are packaged:
```bash
mvn clean package
```

Then lauch the Docker compose:
```bash
docker compose up
```

This will create a single-node Kafka cluster and launch both microservices.

## Running in native

You can compile the application into a native binary using:

`mvn clean install -Pnative`

As you are running in _prod_ mode, you need a Kafka cluster. You can follow the instructions from the [Apache Kafka web site](https://kafka.apache.org/quickstart) or run `docker-compose up` if you have docker installed on your machine.

Then run with:

`./producer/target/kafka-quickstart-producer-1.0.0-SNAPSHOT-runner`
`./processor/target/kafka-quickstart-processor-1.0.0-SNAPSHOT-runner`
