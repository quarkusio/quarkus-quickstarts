# Quarkus demo: Hibernate ORM and RESTEasy

This is a minimal CRUD service exposing a couple of endpoints over REST,
with a front-end based on Angular so you can play with it from your browser.

While the code is surprisingly simple, under the hood this is using:
 - RESTEasy to expose the REST endpoints
 - Hibernate ORM to perform the CRUD operations on the database
 - A PostgreSQL|MariaDB|MySQL database; see below to run one via Docker
 - ArC, the CDI inspired dependency injection tool with zero overhead
 - The high performance Agroal connection pool
 - Infinispan based caching
 - All safely coordinated by the Narayana Transaction Manager

## Requirements

To compile and run this demo you will need:

- JDK 1.8+
- GraalVM

In addition, you will need a database - PostgreSQL, MariaDB or MySQL, or Docker to run one.

### Configuring GraalVM and JDK 1.8+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 1.8+ `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image)
for help setting up your environment.

## Building the demo

Launch the Maven build on the checked out sources of this demo:

> ./mvnw clean install

## Running the demo

### Prepare a database instance

Make sure you have a database instance running - one of PostgreSQL, MariaDB or MySQL.

To set up a PostgreSQL database with Docker:

> docker run --ulimit memlock=-1:-1 -d -it --memory-swappiness=0 --name postgres -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:10.5

To set up a MariaDB database with Docker:

> docker run --ulimit memlock=-1:-1 -d -it --memory-swappiness=0 --name mariadb -e MYSQL_USER=quarkus_test -e MYSQL_PASSWORD=quarkus_test -e MYSQL_DATABASE=quarkus_test -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -p 3306:3306 mariadb:10.3

To set up a MySQL database with Docker:

> docker run --ulimit memlock=-1:-1 -d -it --memory-swappiness=0 --name mysql -e MYSQL_USER=quarkus_test -e MYSQL_PASSWORD=quarkus_test -e MYSQL_DATABASE=quarkus_test -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -p 3306:3306 mysql:8.0

Connection properties for the Agroal datasource are defined in the standard Quarkus configuration file,
`src/main/resources/application.properties`.

### Live coding with Quarkus

The Maven Quarkus plugin provides a development mode that supports
live coding. Try this out. If you have PostgreSQL running:

> ./mvnw clean quarkus:dev

In case you have MariaDB or MySQL running choose appropriate profile:

> ./mvnw clean quarkus:dev -Dquarkus.profile=<mariadb|mysql>

In this mode you can make changes to the code and have the changes immediately applied, by just refreshing your browser.

Hot reload works even when modifying your JPA entities.
Try it! Even the database schema will be updated on the fly.

### Run Quarkus in JVM mode

When you're done iterating in developer mode, you can run the application as a
conventional jar file.

First compile it. For PostgreSQL run:

> ./mvnw clean install

For MariaDB or MySQL choose appropriate profile:

> ./mvnw clean install -Dquarkus.profile=<mariadb|mysql> -Dquarkus.test.profile=<mariadb|mysql>

Then run it:

> java -jar ./target/hibernate-orm-quickstart-1.0-SNAPSHOT-runner.jar

Have a look at how fast it boots.
Or measure total native memory consumption...

### Run Quarkus as a native application

You can also create a native executable from this application without making any
source code changes. A native executable removes the dependency on the JVM:
everything needed to run the application on the target platform is included in
the executable, allowing the application to run with minimal resource overhead.

Compiling a native executable takes a bit longer, as GraalVM performs additional
steps to remove unnecessary codepaths. Use the  `native` profile to compile a
native executable. For PostgreSQL:

> ./mvnw clean install -Dnative

For MariaDB or MySQL choose appropriate profile:

> ./mvnw clean install -Dnative -Dquarkus.profile=<mariadb|mysql> -Dquarkus.test.profile=<mariadb|mysql>

After getting a cup of coffee, you'll be able to run this binary directly:

> ./target/hibernate-orm-quickstart-1.0-SNAPSHOT-runner

Please brace yourself: don't choke on that fresh cup of coffee you just got.
    
Now observe the time it took to boot, and remember: that time was mostly spent to generate the tables in your database and import the initial data.
    
Next, maybe you're ready to measure how much memory this service is consuming.

N.B. This implies all dependencies have been compiled to native;
that's a whole lot of stuff: from the bytecode enhancements that Hibernate ORM
applies to your entities, to the lower level essential components such as the jdbc driver, the Undertow webserver.

## See the demo in your browser

Navigate to:

<http://localhost:8080/index.html>

Have fun, and join the team of contributors!

## Running the demo in Kubernetes

This section provides extra information for running both the database and the demo on Kubernetes.
As well as running the DB on Kubernetes, a service needs to be exposed for the demo to connect to the DB.

Then, rebuild demo docker image with a system property that points to the DB. 

```bash
-Dquarkus.datasource.jdbc.url=jdbc:postgresql://<DB_SERVICE_NAME>/quarkus_test
```

```bash
-Dquarkus.datasource.jdbc.url=jdbc:mariadb://<DB_SERVICE_NAME>/quarkus_test
```

```bash
-Dquarkus.datasource.jdbc.url=jdbc:mysql://<DB_SERVICE_NAME>/quarkus_test
```