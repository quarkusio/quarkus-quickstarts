# Quarkus demo: Redis

This is a simple incrementing service using Redis command.

While the code is surprisingly simple, under the hood this is using:
 - RESTEasy to expose the REST endpoints
 - A Redis database; see below to run one via Docker
 - ArC, the CDI inspired dependency injection tool with zero overhead
 
## Requirements

To compile and run this demo you will need:

- JDK 1.8+
- GraalVM

In addition, you will need either a Redis database, or Docker to run one.

### Configuring GraalVM and JDK 1.8+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 1.8+ `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image)
for help setting up your environment.

## Building the demo

Launch the Maven build on the checked out sources of this demo:

> ./mvnw package

Note that running this command will start a Redis instance and run the tests.

## Running the demo

### Prepare a Redis instance

Make sure you have a Redis instance running. To set up a Redis with Docker:

> docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 --name redis_quarkus_test -p 6379:6379 redis:5.0.6

Connection properties for the Redis connection are defined in the standard Quarkus configuration file,
`src/main/resources/application.properties`.

### Live coding with Quarkus

The Maven Quarkus plugin provides a development mode that supports
live coding. To try this out:

>  mvn quarkus:dev

In this mode you can make changes to the code and have the changes immediately applied, by just making a http request to the service.

### Run Quarkus in JVM mode

When you're done iterating in developer mode, you can run the application as a
conventional jar file.

First compile it:

> ./mvnw package

Note that this command will start a Redis instance to execute the tests.
Thus your Redis containers need to be stopped.

Then run it:

> java -jar ./target/redis-quickstart-1.0-SNAPSHOT-runner.jar

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

> ./target/redis-quickstart-1.0-SNAPSHOT-runner

    Please brace yourself: don't choke on that fresh cup of coffee you just got.
    
    Now observe the time it took to boot, and remember: that time was mostly spent to generate the tables in your database and import the initial data.
    
    Next, maybe you're ready to measure how much memory this service is consuming.

## Exposed endpoints
 - `GET /increments`
 - `GET /increments/{key}`
 - `DELETE /increments/{key}`
 - `POST /increments` accepting `{key:"key", value:int-value}`
 - `PUT /increments/{key}`  accepting an integer representing the increment value
