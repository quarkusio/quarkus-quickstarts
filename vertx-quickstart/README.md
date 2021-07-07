# Getting Started with Vert.x in Quarkus 

* Quarkus guide: https://quarkus.io/guides/vertx

This project contains the code developed in the Quarkus guide, as well as a few extras examples.
Run the examples using: `./mvnw quarkus:dev`

## SockJS example

This example demonstrates how you can send event to a browser from a Quarkus application using the Vert.x SockJS bridge.
Two main files compose this demo:

* [src/main/java/org/acme/extra/SockJsExample.java](./src/main/java/org/acme/extra/SockJsExample.java) - configure the SockJS bridge and send _ticks_ every seconds
* [src/main/resources/META-INF/resources/sockjs-example.html](src/main/resources/META-INF/resources/sockjs-example.html) - a simple web page receiving the events

After having started the application, open your browser to http://localhost:8080/sockjs-example.html.

## Reactive PostgreSQL example

This example demonstrates how you can use the Vert.x PostgreSQL client to implement a reactive CRUD application.
Three main files compose this demo:

* [src/main/java/org/acme/extra/Fruit.java](./src/main/java/org/acme/extra/Fruit.java) - represent Fruits
* [src/main/java/org/acme/extra/FruitResource.java](./src/main/java/org/acme/extra/FruitResource.java) - implement the CRUD HTTP interface
* [src/main/resources/META-INF/resources/fruits.html](src/main/resources/META-INF/resources/fruits.html) - the UI

After having started the application, open your browser to http://localhost:8080/fruits.html.






