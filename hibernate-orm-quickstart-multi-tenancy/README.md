# Supporting Multi-Tenancy in Hibernate ORM

This guide demonstrates how your Hibernate ORM application can support multitenancy so that you can serve multiple tenants from a single application. 

When serving multiple customers from a same application (e.g.: SaaS), each customer is a tenant with a separate database or a separate schema in the same database. 

## Requirements

To compile and run this demo you will need:

- JDK 1.8+
- GraalVM

In addition, you will need either a PostgreSQL database, or Docker to run one.

### Configuring GraalVM and JDK 1.8+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 1.8+ `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image)
for help setting up your environment.

## Building the demo

Launch the Maven build on the checked out sources of this demo:

> ./mvnw install

## Running the demo

### SCHEMA Multitenancy

#### Prepare a single PostgreSQL instance

Make sure you have a PostgreSQL instance running. To set up a PostgreSQL database with Docker:

> docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name quarkus_test -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:12.2

Connection properties for the Agroal datasource are defined in the standard Quarkus configuration file,
`src/main/resources/application.properties`. 
The database schemas are created using [Flyway](https://quarkus.io/guides/flyway) and the configuration can be found in 
`src/main/resources/schema/V1.0.0__create_fruits.sql`

#### Start Quarkus in development mode (Profile 'development')

The Maven Quarkus plugin provides a development mode that supports live coding. To try this out:

> ./mvnw quarkus:dev

In this mode you can make changes to the code and have the changes immediately applied, by just refreshing your browser.

### DATABASE Multitenancy

#### Prepare two PostgreSQL instances

Make sure you have two PostgreSQL instances running. To set up two PostgreSQL databases with Docker:

> docker run -it --rm=true --ulimit memlock=-1:-1 --memory-swappiness=0 --name quarkus_test -p 127.0.0.1:5432:5432 -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test postgres:12.2

> docker run -it --rm=true --ulimit memlock=-1:-1 --memory-swappiness=0 --name mycompany -p 127.0.0.1:5433:5432 -e POSTGRES_USER=mycompany -e POSTGRES_PASSWORD=mycompany -e POSTGRES_DB=mycompany postgres:12.2

The 'quarkus_test' database should be listening on port 5432 and the 'mycompany' on port 5433.

Connection properties for the Agroal datasource are defined in the standard Quarkus configuration file,
`src/main/resources/application.properties`.
The database schemas are created using [Flyway](https://quarkus.io/guides/flyway) and the configuration can be found in 
`src/main/resources/database/default/V1.0.0__create_fruits.sql` and
`src/main/resources/database/mycompany/V1.0.0__create_fruits.sql`.

#### Start Quarkus in development mode (Profile 'database')

The Maven Quarkus plugin provides a development mode that supports live coding. To try this out:

> ./mvnw quarkus:dev -Dquarkus.profile=database

In this mode you can make changes to the code and have the changes immediately applied, by just refreshing your browser.


## See the demo in your browser

Navigate to:

<http://localhost:8080/index.html>

You can easily select the tenant in the dropdown and in the backround the appropriate schema or database will be selected. 

Have fun, and join the team of contributors!

