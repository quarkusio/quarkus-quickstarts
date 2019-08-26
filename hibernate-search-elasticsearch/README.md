# Quarkus demo: Hibernate ORM, Hibernate Search + Elasticsearch and RESTEasy

This is a CRUD service exposing a couple of endpoints over REST,
with a front-end based on Angular so you can play with it from your browser.

While the code is surprisingly simple, under the hood this is using:
 - RESTEasy to expose the REST endpoints
 - Hibernate ORM to perform the CRUD operations on the database
 - Hibernate Search + Elasticsearch to index the entities in an Elasticsearch index
 - A PostgreSQL database; see below to run one via Docker
 - ArC, the CDI inspired dependency injection tool with zero overhead
 - The high performance Agroal connection pool
 - Infinispan based caching
 - All safely coordinated by the Narayana Transaction Manager

## Requirements

To compile and run this demo you will need:
- GraalVM - see [our Building native image guide](https://quarkus.io/guides/building-native-image-guide)
- Apache Maven `3.5.3+`
- Docker

In addition, you will need either a PostgreSQL database, or Docker to run one.

If you don't have GraalVM installed, you can download it here:

<https://github.com/oracle/graal/releases>

Installing GraalVM is very similar to installing any other JDK:
just unpack it, add it to your path, and point the `JAVA_HOME`
and `GRAALVM_HOME` environment variables to it.

You should then use this JDK to run the Maven build.

## Building the demo

After having set GraalVM as your JVM, launch the Maven build on
the checked out sources of this demo:

> ./mvnw package

Note that running this command will start an Elasticsearch cluster, start a PostgreSQL instance and run the tests.

## Running the demo

### Start an Elasticsearch cluster

You can launch a test instance easily from the project directory:

> ./mvnw elasticsearch:runforked -Des.setAwait=true

If you prefer using Docker:

> docker run -it --rm=true --name elasticsearch_quarkus_test -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.0.1

Alternatively you can setup an Elasticsearch instance in any another way.

### Prepare a PostgreSQL instance

First we will need a PostgreSQL database; you can launch one easily if you have Docker installed:

> docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name postgresql_quarkus_test -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:11.3

Alternatively you can setup a PostgreSQL instance in any another way.

The connection properties of the Agroal datasource are configured in the standard Quarkus configuration file, which you will find in
`src/main/resources/application.properties`.

### Run Quarkus in developer mode

To run the application in interactive mode (developer mode):

>  mvn compile quarkus:dev

In this mode you can make changes to the code and have the changes immediately applied, by just refreshing your browser.

    Hot reload works even when modifying your JPA entities.
    Try it! Even the database schema and the Elasticsearch mapping will be updated on the fly.

### Run Quarkus in JVM mode

When you're done playing with "dev-mode" you can run it as a standard Java application.

First compile it:

> ./mvnw package

Note that this command will start a PostgreSQL instance and an Elasticsearch cluster to execute the tests.
Thus your PostgreSQL and Elasticsearch containers need to be stopped.

Then run it:

> java -jar ./target/hibernate-search-elasticsearch-1.0-SNAPSHOT-runner.jar

    Have a look at how fast it boots.
    Or measure total native memory consumption...

### Run Quarkus as a native application

This same demo can be compiled into native code: no modifications required.

This implies that you no longer need to install a JVM on your production environment, as the runtime technology is included in the produced binary, and optimized to run with minimal resource overhead.

Compilation will take a bit longer, so this step is disabled by default;
let's build again by enabling the `native` profile:

> mvn package -Dnative

After getting a cup of coffee, you'll be able to run this binary directly:

> ./target/hibernate-search-elasticsearch-1.0-SNAPSHOT-runner

    Please brace yourself: don't choke on that fresh cup of coffee you just got.
    
    Now observe the time it took to boot, and remember: that time was mostly spent to generate the tables in your database and import the initial data.
    
    Next, maybe you're ready to measure how much memory this service is consuming.

N.B. This implies all dependencies have been compiled to native;
that's a whole lot of stuff: from the bytecode enhancements that Hibernate ORM
applies to your entities, to the lower level essential components such as the PostgreSQL JDBC driver, the Undertow webserver.

## See the demo in your browser

Navigate to:

<http://localhost:8080/>

Have fun, and join the team of contributors!

## Running the demo in Kubernetes

This section provides extra information for running both the database and the demo on Kubernetes.
As well as running the DB on Kubernetes, a service needs to be exposed for the demo to connect to the DB.

Then, rebuild demo docker image with a system property that points to the DB. 

```bash
-Dquarkus.datasource.url=jdbc:postgresql://<DB_SERVICE_NAME>/quarkus_test
```
