Quarkus RabbitMQ 1.0 Quickstart
============================

This project illustrates how you can interact with RabbitMQ using MicroProfile Reactive Messaging.
The complete instructions are available on https://quarkus.io/guides/rabbitmq.

## Start the application in dev mode

In a first terminal, run:

```bash
> mvn -f rabbitmq-quickstart-producer quarkus:dev
```

In a second terminal, run:

```bash
> mvn -f rabbitmq-quickstart-processor quarkus:dev
```  

Then, open your browser to `http://localhost:8080/quotes.html`, and click on the "Request Quote" button.

## Build the application in JVM mode

To build the applications, run:

```bash
> mvn -f rabbitmq-quickstart-producer package
> mvn -f rabbitmq-quickstart-processor package
```

Because we are running in _prod_ mode, we need to provide a RabbitMQ broker.
The [docker-compose.yml](docker-compose.yml) file starts the broker and your application.

Start the broker and the applications using:

```bash
> docker compose up --build
```

Then, open your browser to `http://localhost:8080/quotes.html`, and click on the "Request Quote" button.
 

## Build the application in native mode

To build the applications into native executables, run:

```bash
> mvn -f rabbitmq-quickstart-producer package -Pnative  -Dquarkus.native.container-build=true
> mvn -f rabbitmq-quickstart-processor package -Pnative -Dquarkus.native.container-build=true
```

The `-Dquarkus.native.container-build=true` instructs Quarkus to build Linux 64-bit native executables, which can run inside containers.  

Then, start the system using:

```bash
> export QUARKUS_MODE=native
> docker compose up
```
Then, open your browser to `http://localhost:8080/quotes.html`, and click on the "Request Quote" button.
