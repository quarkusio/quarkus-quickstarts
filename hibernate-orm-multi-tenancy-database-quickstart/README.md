# Supporting Multi-Tenancy in Hibernate ORM (database approach)

This guide demonstrates how your Hibernate ORM application can support multitenancy so that you can serve multiple tenants from a single application. 

When serving multiple customers from a same application (e.g.: SaaS), each customer is a tenant with a separate database. 

For the schema per tenant approach see [Schema Quickstart](../hibernate-orm-multi-tenancy-schema-quickstart/README.md).

## Requirements

To compile and run this demo you will need:

- JDK 17+
- GraalVM

In addition, you will need either a PostgreSQL database, or Docker to run one.

### Configuring GraalVM and JDK 17+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 17+ `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image)
for help setting up your environment.

## Building the demo

Launch the Maven build on the checked out sources of this demo:

> ./mvnw package

## Running the demo

### Start Quarkus in development mode

The Maven Quarkus plugin provides a development mode that supports live coding. To try this out:

> ./mvnw quarkus:dev

In this mode you can make changes to the code and have the changes immediately applied, by just refreshing your browser.

### Run Quarkus as native executable

You can also create a native executable from this application without making any source code changes. A native executable removes the dependency on the JVM:
everything needed to run the application on the target platform is included in the executable, allowing the application to run with minimal resource overhead.

Compiling a native executable takes a bit longer, as GraalVM performs additional steps to remove unnecessary codepaths. Use the  `native` profile to compile  a native executable:

> ./mvnw package -Pnative

After getting a cup of coffee, you'll be able to run this executable directly:

> ./target/hibernate-orm-schema-multi-tenancy-quickstart-1.0.0-SNAPSHOT-runner


## See the demo in your browser

Navigate to:

<http://localhost:8080/index.html>

You can easily select the tenant in the dropdown and in the background the appropriate database will be selected. 

Have fun, and join the team of contributors!