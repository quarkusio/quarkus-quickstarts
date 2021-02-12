# Quarkus - Context Propagation

## Requirements

To compile and run this demo you will need:

- JDK 1.8+
- GraalVM
- Docker (and Docker Compose)
- httpie (or curl)

### Configuring GraalVM and JDK 1.8+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 1.8+ `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image)
for help setting up your environment.

## Running the demo

### Prepare Kafka and PostGreSQL

Just use the `docker-compose.yaml` file provided in the source:

> docker-compose up


### Running the demo

The Maven Quarkus plugin provides a development mode that supports
live coding. To try this out:

> ./mvnw quarkus:dev

Then, you need 2 terminals.
In the first one, run:

> http :8080/prices --stream

This command streams the next 3 prices.

Send the prices from another terminal with:

> http POST :8080 value:=34
> http POST :8080 value:=35
> http POST :8080 value:=36

The first terminal should have displayed the prices and returns to the prompt.
Then run, from any terminal:

> http :8080/prices/all

It should display the persisted prices. These prices are only persisted when the streams completes (so after having received 3 prices).

### Run Quarkus as a native application

You can also create a native executable from this application without making any
source code changes. A native executable removes the dependency on the JVM:
everything needed to run the application on the target platform is included in
the executable, allowing the application to run with minimal resource overhead.

Compiling a native executable takes a **bit** longer, as GraalVM performs additional
steps to remove unnecessary codepaths. Use the  `native` profile to compile a
native executable:

> ./mvnw package -Dnative

After getting a cup of coffee, you'll be able to run this binary directly:

> ./target/context-propagation-quickstart-1.0.0-SNAPSHOT-runner
