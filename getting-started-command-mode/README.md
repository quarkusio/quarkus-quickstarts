# Getting started with Quarkus command mode

This is a minimal command mode application that access Quarkus API's.

Under the hood, this demo uses:

- `@QuarkusMain` to enable a custom main class

## Requirements

To compile and run this demo you will need:

- JDK 17+
- GraalVM

### Configuring GraalVM and JDK 17+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 17+ `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image-guide)
for help setting up your environment.

## Building the application

Launch the Maven build on the checked out sources of this demo:

> ./mvnw package

### Live coding with Quarkus

The Maven Quarkus plugin provides a development mode that supports
live coding. To try this out:

> ./mvnw quarkus:dev

or

> ./mvnw quarkus:dev -Dquarkus.args=<args>

if you want to pass arguments in.

This command will run the command mode application and if you press enter run it again.
 
1. Make a change in [src/main/java/org/acme/getting/started/commandmode/GreetingResource.java](src/main/java/org/acme/getting/started/commandmode/GreetingService.java). Replace `hello` with `hello there` in the `greeting()` method.
    - Press Enter in your terminal. 
      You should now see `hello there`.
    - Undo the change, so the method returns `hello` again.
    - Press Enter again. You should now see `hello`.

### Run Quarkus in JVM mode

When you're done iterating in developer mode, you can run the application as a
conventional jar file.

First compile it:

> ./mvnw package

Then run it:

> java -jar ./target/quarkus-app/quarkus-run.jar

Have a look at how fast it boots, or measure the total native memory consumption.

### Run Quarkus as a native executable

You can also create a native executable from this application without making any
source code changes. A native executable removes the dependency on the JVM:
everything needed to run the application on the target platform is included in
the executable, allowing the application to run with minimal resource overhead.

Compiling a native executable takes a bit longer, as GraalVM performs additional
steps to remove unnecessary codepaths. Use the  `native` profile to compile a
native executable:

> ./mvnw package -Dnative

After getting a cup of coffee, you'll be able to run this executable directly:

> ./target/getting-started-command-mode-1.0.0-SNAPSHOT-runner
