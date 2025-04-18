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
 - All safely coordinated by the Narayana Transaction Manager

## Requirements

To compile and run this demo you will need:

- JDK 17+
- GraalVM

In addition, you will need a PostgreSQL database and an Elasticsearch instance, or Docker to run them.

### Configuring GraalVM and JDK 17+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 17+ `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image)
for help setting up your environment.

## Building the demo

Launch the Maven build on the checked out sources of this demo:

> ./mvnw package

Note that running this command will start an Elasticsearch cluster, start a PostgreSQL instance and run the tests.

## Running the demo

### Live coding with Quarkus

The Maven Quarkus plugin provides a development mode that supports
live coding. To try this out:

> ./mvnw quarkus:dev

In this mode you can make changes to the code and have the changes immediately applied, by just refreshing your browser.

Dev Mode automatically starts a Docker container with a Postgres database. This feature is called ["Dev Services."](https://quarkus.io/guides/dev-services)

To access the database from the terminal, run:

```sh
docker exec -it <container-name> psql -U quarkus
```

    Hot reload works even when modifying your JPA entities.
    Try it! Even the database schema and the Elasticsearch mapping will be updated on the fly.

### Run Quarkus in JVM mode

When you're done iterating in developer mode, you can run the application as a
conventional jar file.

First compile it:

> ./mvnw package

Note that this command will start a PostgreSQL instance and an Elasticsearch cluster to execute the tests.
Thus your PostgreSQL and Elasticsearch containers need to be stopped.

Next, make sure you have a PostgreSQL database running and an ElasticSearch instance. 
In production, Quarkus does not start a container for you like it does in Dev Mode.
(Quarkus automatically starts one of each for dev and test mode, but not for prod mode).

To set up a PostgreSQL database using Docker:

> docker run -it --rm=true --name postgresql_quarkus_test -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:13.3

To set up an Elasticsearch instance using Docker:

> docker run -it --rm=true --name elasticsearch_quarkus_test -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:8.12.0

Then run the application:

> java -jar ./target/quarkus-app/quarkus-run.jar

    Have a look at how fast it boots.
    Or measure total native memory consumption...

### Run Quarkus as a native application

You can also create a native executable from this application without making any
source code changes. A native executable removes the dependency on the JVM:
everything needed to run the application on the target platform is included in
the executable, allowing the application to run with minimal resource overhead.

Compiling a native executable takes a bit longer, as GraalVM performs additional
steps to remove unnecessary codepaths. Use the  `native` profile to compile a
native executable:

> ./mvnw package -Dnative

After getting a cup of coffee, you'll be able to run this binary directly:

> ./target/hibernate-search-orm-elasticsearch-quickstart-1.0.0-SNAPSHOT-runner

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

## Running the demo on Kubernetes

To run the demo on Kubernetes, you will need to define resources:

* A `Deployment` running the application's container image.
* A `Service` and `Route` pointing to the application to expose it outside of the cluster.
* A `Deployment` or `StatefulSet` running the database.
* A `Service` pointing to the database to expose it to the application.
* A `Deployment` or `StatefulSet` running Elasticsearch.
* A `Service` pointing to Elasticsearch to expose it to the application.

Then, make sure the `Deployment` running the application uses
environment variables that point to the database/Elasticsearch `Services`:

```bash
QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://<DB_SERVICE_NAME>/quarkus_test
QUARKUS_HIBERNATE_SEARCH_ORM_ELASTICSEARCH_HOSTS=<ELASTICSEARCH_SERVICE_NAME>:9200
```
