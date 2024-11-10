# Quarkus demo: OptaPlanner AI with Hibernate ORM and RESTEasy

This project contains a [Quarkus](https://quarkus.io/) application
with [OptaPlanner](https://www.optaplanner.org/)'s constraint solving Artificial Intelligence (AI)
integrated with a database and exposed through a REST API.

This web application optimizes a school timetable for students and teachers.
It assigns `Lesson` instances to `Timeslot` and `Room` instances automatically
by using AI to adhere to hard and soft scheduling constraints, such as:

* *Room conflict*: A room can have at most one lesson at the same time.
* *Teacher conflict*: A teacher can teach at most one lesson at the same time.
* *Student group conflict*: A student can attend at most one lesson at the same time.
* *Teacher room stability*: A teacher prefers to teach in a single room.
* *Teacher time efficiency*: A teacher prefers to teach sequential lessons and dislikes gaps between lessons.
* *Student group subject variety*: A student group dislikes sequential lessons on the same subject.

## Run the application with live coding

1. Start the application:

    ```
    ./mvnw quarkus:dev
    ```

2. Visit http://localhost:8080 in your browser.
3. Click on the _Solve_ button.

Now try live coding:

1. Make some changes in the source code
2. Refresh your browser (F5).

Those changes are immediately applied.

## Package and run the application

When you're done iterating in `quarkus:dev` mode, run the application as a conventional jar file.

1. Compile it:

    ```
    ./mvnw package
    ```

2. run it:

    ```
    java -jar ./target/quarkus-app/quarkus-run.jar
    ```

Look at how fast it boots!

## Run a native executable

You can also create a native executable from this application without making any
source code changes. A native executable removes the dependency on the JVM:
everything needed to run the application on the target platform is included in
the executable, allowing the application to run with minimal resource overhead.

Because the quarkus H2 extension does not support compiling the embedded database engine into native images,
you need to run the H2 server locally first:

 1. Download the [H2 engine](http://www.h2database.com/html/download.html) (Platform-independent zip)
 
 2. Unzip it.

 3. Start H2 server with the option `-ifNotExists` (this is not recommended in production but saves you from creating the database manually)

    ```shell script
    cd h2/bin && java -cp h2*.jar org.h2.tools.Server -ifNotExists
    ```

The database connection in configured in the `application.properties` file,
specifically with the `%prod.quarkus.datasource.*` properties.


Compiling a native executable takes a bit of time,
 as GraalVM performs additional steps to remove unnecessary codepaths.
 Use `-Dnative` to compile a native executable:

```
./mvnw package -Dnative
```
After getting a cup of coffee, run this binary directly:

```
./target/optaplanner-quickstart-1.0.0-SNAPSHOT-runner
```
