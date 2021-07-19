# Quarkus guide: https://quarkus.io/guides/transaction

This quickstart shows how to JTA transactions with core quarkus features like JAX-RS and async support.
It does not perform actual transactional work since there are other quickstarts and extensions for that
(such as hibernate-orm, hibernate-reactive, agroal, etc).

It uses

- RESTEasy to expose REST endpoints which demonstrate various aspects of transactional behaviour
- REST-assured and JUnit 5 for endpoint testing

## Requirements

To compile and run this demo you will need:

- JDK 1.8+
- GraalVM

### Configuring GraalVM and JDK 1.8+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 1.8+ `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image-guide)
for help setting up your environment.

## Building the application

Launch the Maven build on the checked out sources of this demo:

> ./mvnw package

This command will build the example and run the tests.

### Live coding with Quarkus

The Maven Quarkus plugin provides a development mode that supports
live coding. To try this out:

> ./mvnw quarkus:dev

This command will leave Quarkus running in the foreground listening on port 8080.

### Run Quarkus in JVM mode

When you're done iterating in developer mode, you can run the application as a
conventional jar file.

An alternative to running the tests is to use a tool such as curl to exercise the endpoints:

> java -jar ./target/jta-1.0-SNAPSHOT-runner.jar

non transactional
> curl -XGET http://0.0.0.0:8080/jta # This should return the value 6 which corresponds to no transaction

Do transactional work by annotating a resource method with @Transactional
(the framework will manage the transaction boundaries):

> curl -XPOST http://0.0.0.0:8080/jta/cmt # this should return 0 which corresponds to an active transaction

Do transactional work by explicitly managing the transaction boundaries:

> curl -XPOST http://0.0.0.0:8080/jta/bmt # This should return the value 6 which corresponds to no transaction

Do transactional work in an asynchronous JAX-RS method using AsyncResponse and MicroProfile Context Propagation:

> curl -XPOST http://0.0.0.0:8080/jta/async-with-suspended # This should return the value 3 which corresponds to an transaction

Do transactional work in a completion stage:

> curl -XPOST http://0.0.0.0:8080/jta/async-with-completion-stage # this should return 0 which corresponds to an active transaction

There is also an endpoint for reproducing an open quarkus issue (6471). It is disabled in the tests pending the fix: 

> curl -XPOST http://0.0.0.0:8080/jta/async-6471-reproducer

### Run Quarkus as a native executable

You can also create a native executable from this application without making any
source code changes. A native executable removes the dependency on the JVM:
everything needed to run the application on the target platform is included in
the executable, allowing the application to run with minimal resource overhead.

Compiling a native executable takes a bit longer, as GraalVM performs additional
steps to remove unnecessary codepaths. Use the  `native` profile to compile a
native executable:

> ./mvnw package -Dnative

and then run this executable directly:

> ./target/jta-quickstart-1.0-SNAPSHOT-runner
