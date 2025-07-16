# Getting started with Quarkus

This is a CRUD service exposing endpoints over REST. These endpoints are used for get, add, update
and
remove entries from database.

## Requirements

To compile and run this demo you will need:

- JDK 11+
- GraalVM
- httpie (or curl)

### Configuring GraalVM and JDK 11+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have been set, and that
a JDK 11+ `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image-guide)
for help setting up your environment.

## Building the application

Launch the Maven build on the checked out sources of this demo:

> ./mvnw package

### Running the demo

The Maven Quarkus plugin provides a development mode that supports live coding. To try this out:

> ./mvnw quarkus:dev

This command will leave Quarkus running in the foreground listening on port 8080.

Launch the second terminal which will be used to send a request.

To get list of all fruits visit [http://127.0.0.1:8080/fruits](http://127.0.0.1:8080/fruits) and all
fruit in database should be returned or you can use terminal and get all fruits by one of these
commands:
> http :8080/fruits
>
> curl 127.0.0.1:8080/fruits

The output of these command should display same content as browser.

You can also get fruit by id. In this example used id is equal to 1. Get fruit by id is
possible either by using browser or by terminal. To get fruit by browser
visit [http://127.0.0.1:8080/fruits/1](http://127.0.0.1:8080/fruits/1) and info about kiwi fruit
should be displayed. To get fruit by terminal:
> http :8080/fruits/1
>
> curl 127.0.0.1:8080/fruits/1

The output of these command should display same content as browser.

Next we add lemon fruit. To add fruit the POST request needs to be sent. This request must contain
key-value pair. In this case `name={fruitName}`. To add fruit send request from terminal:
> http POST :8080/fruits name=Lemon
>
> curl -X POST http://localhost:8080/fruits -H 'Content-Type: application/json' -d '{"name":"Lemon"}'

This request create fruit with name `Lemon`. Now you can check all fruits and the newly added fruit
should be visible.

To update name of fruit the PUT request needs to be sent. This request must contain two key-value
pairs. In this case `id={fruitId}` and `name={newFruitName}`. In this example used id is equal to 1.
To update fruit name send request from terminal:
> http PUT :8080/fruits/1 id=1 name=Pitaya
>
> curl -X PUT http://localhost:8080/fruits/1 -H "Content-Type: application/json" -d '{"id":1,
> "name":"Pitaya"}'

This request update name of fruit with index 1 to `Pitaya`. Now you can check all fruits and fruit
with index 1 should be updated.

Now as you know how to show, add and update fruits last step will be to delete fruit. To delete
fruit you need to know its id. In case of this example the fruit with id equal to 1 will be
deleted. To delete fruit the DELETE request needs to be sent. To delete fruit send request from
terminal:
> http DELETE :8080/fruits/1
>
> curl -X DELETE http://localhost:8080/fruits/1

Check that fruit with id 1 is not existing anymore.

### Run Quarkus in JVM mode

When you're done iterating in developer mode, you can run the application as a conventional jar
file.

First compile it:

> ./mvnw package

Then run it:

> java -jar ./target/quarkus-app/quarkus-run.jar

Have a look at how fast it boots, or measure the total native memory consumption.

### Run Quarkus as a native executable

You can also create a native executable from this application without making any source code
changes. A native executable removes the dependency on the JVM: everything needed to run the
application on the target platform is included in the executable, allowing the application to run
with minimal resource overhead.

Compiling a native executable takes a bit longer, as GraalVM performs additional steps to remove
unnecessary codepaths. Use the `native` profile to compile a native executable:

> ./mvnw package -Dnative

After getting a cup of coffee, you'll be able to run this executable directly:

> ./target/getting-started-reactive-rest-1.0.0-SNAPSHOT-runner
