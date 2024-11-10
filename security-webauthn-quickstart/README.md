Quarkus Security with WebAuthn
========================

This guide demonstrates how your Quarkus application can use a database and WebAuthn to store your user credentials.

## Start the database

You need a database to store the user identities/credentials. Here, we are using [PostgreSQL](https://www.postgresql.org).
To ease the setup, we have provided a `docker-compose.yml` file which start a PostgreSQL container and bind the network ports.

The database can be started using:
 ```bash
 docker-compose up
 ```

Once the database is up you can start your Quarkus application.

Note you do not need to start the database when running your application in dev mode or testing. It will be started automatically as a Dev Service.

## Start the application

The application can be started using: 

```bash
mvn compile quarkus:dev
```  

## Test the application

### From  the CLI
The application exposes 4 endpoints:
* `/api/public`
* `/api/public/me`
* `/api/admin`
* `/api/users/me`

You can try these endpoints with a browser, using a hardware token by visiting http://localhost:8080. 

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

`./target/security-jpa-webauthn-1.0.0-SNAPSHOT-runner` 

_NOTE:_ Don't forget to configure and start your database if you run without DEV services.
