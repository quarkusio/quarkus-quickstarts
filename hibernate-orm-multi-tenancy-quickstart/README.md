# Supporting Multi-Tenancy in Hibernate ORM

This guide demonstrates how your Hibernate ORM application can support multitenancy so that you can serve multiple tenants from a single application. 

When serving multiple customers from a same application (e.g.: SaaS), each customer is a tenant with a separate database or a separate schema in the same database. 

## Requirements

To compile and run this demo you will need:

- JDK 11+
- GraalVM

In addition, you will need either a PostgreSQL database, or Docker to run one.

### Configuring GraalVM and JDK 11+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 11+ `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image)
for help setting up your environment.

## Building the demo

Launch the Maven build on the checked out sources of this demo:

> ./mvnw package

## Running the demo

### SCHEMA Multitenancy

Approach that uses a single database (default datasource) with two schemas ('base' and 'mycompany') for storing the tenant's data.

#### Prepare a single PostgreSQL instance

Make sure you have a PostgreSQL instance running. To set up a PostgreSQL database with Docker:

> docker run -it --rm=true --name quarkus_test -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:13.3

Connection properties for the Agroal datasource are defined in the standard Quarkus configuration file, [src/main/resources/application.properties](src/main/resources/application.properties). The database schemas are created using [Flyway](https://quarkus.io/guides/flyway) and the configuration can be found in 
[src/main/resources/schema/V1.0.0__create_fruits.sql](src/main/resources/schema/V1.0.0__create_fruits.sql).

#### Start Quarkus in development mode (Profile 'development')

The Maven Quarkus plugin provides a development mode that supports live coding. To try this out:

> ./mvnw quarkus:dev

In this mode you can make changes to the code and have the changes immediately applied, by just refreshing your browser.

#### Run Quarkus as native executable

You can also create a native executable from this application without making any source code changes. A native executable removes the dependency on the JVM:
everything needed to run the application on the target platform is included in the executable, allowing the application to run with minimal resource overhead.

Compiling a native executable takes a bit longer, as GraalVM performs additional steps to remove unnecessary codepaths. Use the  `native` profile to compile a
native executable:

> ./mvnw package -Pnative

After getting a cup of coffee, you'll be able to run this executable directly:

> ./target/hibernate-orm-quickstart-multi-tenancy-1.0.0-SNAPSHOT-runner

### DATABASE Multitenancy

Approach that uses a separate database (datasource 'base' and 'mycompany') for storing the tenant's data.

#### Prepare two PostgreSQL instances

Make sure you have two PostgreSQL instances running. To set up two PostgreSQL databases with Docker:

> docker run -it --rm=true --name quarkus_test -p 127.0.0.1:5432:5432 -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test postgres:13.3

> docker run -it --rm=true --name mycompany -p 127.0.0.1:5433:5432 -e POSTGRES_USER=mycompany -e POSTGRES_PASSWORD=mycompany -e POSTGRES_DB=mycompany postgres:13.3

The 'quarkus_test' database should be listening on port 5432 and the 'mycompany' on port 5433.

Connection properties for the Agroal datasource are defined in the standard Quarkus configuration file, [src/main/resources/application.properties](src/main/resources/application.properties).
The database schemas are created using [Flyway](https://quarkus.io/guides/flyway) and the configuration can be found in 
[src/main/resources/database/base/V1.0.0__create_fruits.sql](src/main/resources/database/base/V1.0.0__create_fruits.sql) and
[src/main/resources/database/mycompany/V1.0.0__create_fruits.sql](src/main/resources/database/mycompany/V1.0.0__create_fruits.sql).

#### Start Quarkus in development mode (Profile 'database')

The Maven Quarkus plugin provides a development mode that supports live coding. To try this out:

> ./mvnw quarkus:dev -Dquarkus.profile=database

In this mode you can make changes to the code and have the changes immediately applied, by just refreshing your browser.

#### Run Quarkus as native executable

You can also create a native executable from this application without making any source code changes. A native executable removes the dependency on the JVM:
everything needed to run the application on the target platform is included in the executable, allowing the application to run with minimal resource overhead.

Compiling a native executable takes a bit longer, as GraalVM performs additional steps to remove unnecessary codepaths. Use the  `native` profile to compile  a native executable:

> ./mvnw package -Pnative -Dquarkus.profile=database

The `-Dquarkus.profile=database` parameter will create an executable that has the 'database' profile enabled.
This means all values in [application.properties](src/main/resources/application.properties) that are prefixed with `%database.` will be active. This enables the Hibernate multi-tenancy DATABASE approach for the example.

After getting a cup of coffee, you'll be able to run this executable directly:

> ./target/hibernate-orm-quickstart-multi-tenancy-1.0.0-SNAPSHOT-runner



## See the demo in your browser

Navigate to:

<http://localhost:8080/index.html>

You can easily select the tenant in the dropdown and in the backround the appropriate schema or database will be selected. 

Have fun, and join the team of contributors!

