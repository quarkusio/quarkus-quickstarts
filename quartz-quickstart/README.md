# Quarkus demo: Quartz scheduling

This is an application demoing a clustered scheduler.
It exposes a REST API `tasks` to visualize the executed jobs which run every 10 seconds. 

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

### Live coding with Quarkus

The Maven Quarkus plugin provides a development mode that supports
live coding. To try this out:

>  mvn compile quarkus:dev

In this mode you can make changes to the code and have the changes immediately applied, by just refreshing your browser.

### Run Quarkus in JVM mode

When you're done iterating in developer mode, you can run the application as a
conventional jar file.

First compile it:

> ./mvnw package

Note that running this command will start a PostgreSQL instance and run the tests. To skip tests use the command below

> ./mvnw package -DskipTests

Next we need to make sure you have a PostgreSQL instance running (Quarkus automatically starts one for dev and test mode). To set up a PostgreSQL database with Docker:

> docker-compose up postgres

Connection properties for the Agroal datasource, and Quartz are defined in the standard Quarkus configuration file,
`src/main/resources/application.properties`.

Then run the application with:

> docker-compose up --scale tasks=2 --scale nginx=1

To start two instances of single instance of the application along side the nginx load balancer.

## Run Quarkus as a native application

You can also create a native executable from this application without making any
source code changes. A native executable removes the dependency on the JVM:
everything needed to run the application on the target platform is included in
the executable, allowing the application to run with minimal resource overhead.

Compiling a native executable takes a bit longer, as GraalVM performs additional
steps to remove unnecessary codepaths. Use the  `native` profile to compile a
native executable:

> ./mvnw package -Dnative

After getting a cup of coffee, you'll be able to run this binary directly:

> ./target/quartz-quickstart-1.0.0-SNAPSHOT-runner

Please brace yourself: don't choke on that fresh cup of coffee you just got.

Now observe the time it took to boot, and remember: that time was mostly spent to migrate the `quartz` tables in your database.

## Visualize created tasks

> curl http://localhost:8080/tasks

Have fun, and join the team of contributors!
