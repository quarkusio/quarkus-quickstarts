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

### Live coding with Quarkus

The Maven Quarkus plugin provides a development mode that supports
live coding. To try this out:

> ./mvnw quarkus:dev

In this mode you can make changes to the code and have the changes immediately applied, by just refreshing your browser.

Dev Mode automatically starts two Docker containers running Postgres databases. This feature is called ["Dev Services."](https://quarkus.io/guides/dev-services)

To access the database from the terminal, run:

```sh
docker container list
```

To get the id or the name of the created containers and then to access the database machine use 

```sh
docker exec -it <container-name> psql -U quarkus -d base
docker exec -it <container-name> psql -U quarkus -d mycompany
```

As the ids and names are auto generated, you should try the command with both ids or names to see what works.
If the wrong one is provided, you'll get a `FATAL:  role "base" does not exist`. Try the other container instance instead.

    Hot reload works even when modifying your JPA entities.
    Try it! Even the database schema will be updated on the fly.

### Run Quarkus in JVM mode

When you're done iterating in developer mode, you can run the application as a
conventional jar file.

First compile it:

> ./mvnw package

Next, make sure you have the two PostgreSQL databases running. In production, Quarkus does not start any container for you like it does in Dev Mode.
To set up two PostgreSQL databases with Docker:

```sh

docker run -it --rm=true --name base-container -e POSTGRES_USER=base -e POSTGRES_PASSWORD=base -e POSTGRES_DB=base -p 5432:5432 postgres:13.3
docker run -it --rm=true --name mycompany-container -e POSTGRES_USER=mycompany -e POSTGRES_PASSWORD=mycompany -e POSTGRES_DB=mycompany -p 5433:5432 postgres:13.3
```

Connection properties for the Agroal datasource are defined in the standard Quarkus configuration file,
`src/main/resources/application.properties`.

To access these databases, since the usernames and passwords are set inside the `%prod` profile, and the usernames are the same as the Postgres database names, we can use to access those:

```sh
docker exec -it base-container psql -U base
docker exec -it mycompany-container psql -U mycompany
```


Then run it:

> java -jar ./target/quarkus-app/quarkus-run.jar

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

> ./target/hibernate-orm-schema-multi-tenancy-quickstart-1.0.0-SNAPSHOT-runner

    Please brace yourself: don't choke on that fresh cup of coffee you just got.
    
    Now observe the time it took to boot, and remember: that time was mostly spent to generate the tables in your database and import the initial data.
    
    Next, maybe you're ready to measure how much memory this service is consuming.

N.B. This implies all dependencies have been compiled to native;
that's a whole lot of stuff: from the bytecode enhancements that Hibernate ORM
applies to your entities, to the lower level essential components such as the PostgreSQL JDBC driver, the Undertow webserver.

## See the demo in your browser

Navigate to:

<http://localhost:8080/index.html>

You can easily select the tenant in the dropdown and in the background the appropriate database will be selected. 

Have fun, and join the team of contributors!