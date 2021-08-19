# Quarkus Artemis JMS

This project illustrates how you can use Artemis JMS with Quarkus.

## Artemis server

First you need an Apache ActiveMQ Artemis server. You can follow the instructions from the [Apache Artemis web site](https://activemq.apache.org/components/artemis/) or run via docker using the [ArtemisCloud](https://artemiscloud.io/) container image:

```bash
docker run -it --rm -p 8161:8161 -p 61616:61616 -p 5672:5672 -e AMQ_USER=quarkus -e AMQ_PASSWORD=quarkus quay.io/artemiscloud/activemq-artemis-broker:0.1.4
```

## Start the application

The application can be started using:

```bash
mvn quarkus:dev
```

Then, open your browser to `http://localhost:8080/prices.html`, and you should see a button to fetch the last price.

## Anatomy

In addition to the `prices.html` page, the application is composed by 3 components:

* `PriceProducer` - the `PriceProducer` sends random prices to a JMS queue.
* `PriceConsumer` - the `PriceConsumer` receives the JMS message and stores the last price.
* `PriceResource`  - the `PriceResource` is able to sent the last price of the PriceConsumer back to the browser.

The configuration is located in the application configuration.

## Running in native

You can compile the application into a native binary using:

`mvn clean install -Pnative`

and run with:

`./target/jms-quickstart-1.0.0-SNAPSHOT-runner`
