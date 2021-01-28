Quarkus MQTT Quickstart
========================

This project illustrates how you can interact with MQTT using MicroProfile Reactive Messaging.

## MQTT server

First you need a MQTT server. You can follow the instructions from the [Eclipse Mosquitto](https://mosquitto.org/) or run `docker-compose up` if you have docker installed on your machine.

## Start the application

The application can be started using: 

```bash
mvn quarkus:dev
```  

Then, open your browser to http://localhost:8080/prices.html, and you should see a fluctuating price.

## Anatomy

In addition to the `prices.html` page, the application is composed by 3 components:

* `PriceGenerator` - a bean generating random price. They are sent to a MQTT topic
* `PriceConverter` - on the consuming side, the `PriceConverter` receives the MQTT message and convert the price.
The result is sent to an in-memory stream of data
* `PriceResource`  - the `PriceResource` retrieves the in-memory stream of data in which the converted prices are sent and send these prices to the browser using Server-Sent Events.

The interaction with MQTT is managed by MicroProfile Reactive Messaging.
The configuration is located in the application configuration.

## Running in native

You can compile the application into a native binary using:

`mvn clean install -Pnative`

and run with:

`./target/mqtt-quickstart-1.0.0-SNAPSHOT-runner` 
