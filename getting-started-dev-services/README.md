# Getting started with Quarkus

This is a minimal CRUD service exposing a couple of endpoints over REST.

Under the hood, this demo uses:

- RESTEasy to expose the REST endpoints
- REST-assured and JUnit 5 for endpoint testing
- Panache and Hibernate for persistence 
- Dev services 

## Requirements

To compile and run this demo you will need:

- JDK 17+
- A container runtime, such as Docker or Podman

### Configuring JDK 17+

Make sure that the `JAVA_HOME` environment variables have
been set, and that a JDK 17+ `java` command is on the path.

### Live coding with Quarkus

The Maven Quarkus plugin provides a development mode that supports
live coding. To try this out:

> ./mvnw quarkus:dev

This command will leave Quarkus running in the foreground listening on port 8080.

1. Visit the default endpoint: [http://127.0.0.1:8080](http://127.0.0.1:8080).
   - Make a simple change to [src/main/resources/META-INF/resources/index.html](src/main/resources/META-INF/resources/index.html) file.
   - Refresh the browser to see the updated page.
2. Visit the `/hello` endpoint: [http://127.0.0.1:8080/hello](http://127.0.0.1:8080/hello)
   - Update the response in [src/main/java/org/acme/quickstart/GreetingResource.java](src/main/java/org/acme/quickstart/GreetingResource.java). Replace `hello` with `hello there` in the `hello()` method.
   - Refresh the browser. You should now see `hello there`.
   - Undo the change, so the method returns `hello` again.
   - Refresh the browser. You should now see `hello`.

### Run Quarkus in JVM mode

When you're done iterating in developer mode, you can run the application as a
conventional jar file.

First compile it:

> ./mvnw package

Then run it:

> java -jar ./target/quarkus-app/quarkus-run.jar

Have a look at how fast it boots, or measure the total native memory consumption.

