Quarkus Security with JPA
========================

This guide demonstrates how your Quarkus application can use a database and JPA to store your user identities.

## Run quickstart in developer mode

Quarkus provides developer mode, in which you can try this example. Just try:

```bash
mvn quarkus:dev
```

Now the application will listen on `localhost:8080`.
In developer mode quarkus will also start its own postgres database.

## Run quickstart in JVM mode

### Start the database

Now we need to start a [PostgreSQL](https://www.postgresql.org) database on our own.
To set it up with docker:

```bash
docker run -it --rm=true --name quarkus_test -e POSTGRES_USER=quarkus -e POSTGRES_PASSWORD=quarkus -e POSTGRES_DB=quarkus -p 5432:5432 postgres:15.3
```

Once the database is up, you can start your Quarkus application.
Application will fill in the users and their credentials on `StartupEvent`.

### Start the application

The application can be build & started using: 

```bash
mvn clean package
java -jar target/quarkus-app/quarkus-run.jar 
```  

## Test the application

### From  the CLI
The application exposes 3 endpoints:
* `/api/public`
* `/api/admin`
* `/api/users/me`

You can try these endpoints with a http client (`curl`, `HTTPie`, etc).
Here you have some examples to check the security configuration:

```bash
curl -i -X GET http://localhost:8080/api/public  # 'public'
curl -i -X GET http://localhost:8080/api/admin  # unauthorized
curl -i -X GET -u admin:admin http://localhost:8080/api/admin # 'admin'
curl -i -X GET http://localhost:8080/api/users/me # 'unauthorized'
curl -i -X GET -u user:user http://localhost:8080/api/users/me # 'user'
```

### Integration testing

We have provided integration tests based on [Dev Services for PostgreSQL](https://quarkus.io/guides/dev-services#databases) to verify the security configuration in JVM and native modes. The test and dev modes containers will be launched automatically because all the PostgreSQL configuration properties are only enabled in production (`prod`) mode.


The test can be executed using: 

```bash
# JVM mode
mvn test

# Native mode
mvn verify -Pnative
```

## Running in native

You can compile the application into a native binary using:

`mvn clean package -Pnative`

_Note: You need to have a proper GraalVM configuration to build a native binary._

and run with:

`./target/security-jpa-quickstart-1.0.0-SNAPSHOT-runner` 

_NOTE:_ Don't forget to start the database.
