# Getting started with the Quarkus Mailer

* Quarkus guide: https://quarkus.io/guides/mailer

This is a minimal application sending emails with Gmail.
It follows the instructions from the getting started guide, but also contains some extra examples:

* [using template](src/main/java/org/acme/extra/ExtraMailResource.java) 
* [using type safe template](src/main/java/org/acme/extra/TypeSafeMailResource.java)
* [using attachments](src/main/java/org/acme/extra/ExtraMailResource.java)

## Prerequisites

1. You need to generate an application password to use GMail from the application.
Follow these [instructions](https://support.google.com/mail/answer/185833) to create the password. 

2. You need Java 11+.
3. You need GraalVM and `native-image` installed and configured. Follows the [instructions](https://quarkus.io/guides/building-native-image) to download, install, and configure GraalVM.

## Building the application

Launch the Maven build on the checked-out sources of this demo:

```shell script
> ./mvnw package
```

Generate the native executable using:

```shell script
> ./mvnw package -Dnative
```

The application contains tests that used the _mock_ mailer to avoid sending actual emails during the tests.
The dev mode also uses this _mock_ mailer.

## Configuring the application

The application uses GMAIL.
Define the following environment properties:

* `QUARKUS_MAILER_USERNAME` - your Google email address
* `QUARKUS_MAILER_FROM` - your Google email address (or alias)
* `QUARKUS_MAILER_PASSWORD` - the application password generated above

### Running the application in JVM mode

Run the application with:

```shell script
> java -jar ./target/quarkus-app/quarkus-run.jar
```

Thanks to the environment properties defined above, the application should authenticate with Gmail and send the email.
To send the email, use the `/mail` endpoint.

Before doing so:

1. Change the email address used in `src/main/java/org/acme/MailResource.java`, so you can receive the email
2. If you want to send an email (and not just simulate it), add `quarkus.mailer.mock=false` to the `src/main/resources/application.properties` file

Then, in a terminal use

```shell script
curl -v :8080/mail
```

### Running the application as a native executable

You can also create a native executable from this application without making any
source code changes. A native executable removes the dependency on the JVM:
everything needed to run the application on the target platform is included in
the executable, allowing the application to run with minimal resource overhead.

Compiling a native executable takes a bit longer, as GraalVM performs additional
steps to remove unnecessary code paths. Use the  `native` profile to compile a
native executable:

```shell script
> ./mvnw package -Dnative
```

After getting a cup of coffee, you'll be able to run this executable directly:

```shell script
> ./target/mailer-quickstart-1.0.0-SNAPSHOT-runner
```

Then send an email using the same _curl_ command. 


## Other demos

Before running the other demos, don't forget to edit the code to use your email address.

### Using attachments

* Code: [using attachments](src/main/java/org/acme/extra/ExtraMailResource.java)
* Command: `curl -v http://localhost:8080/extra/attachment`

### Using template

* Code: [using template](src/main/java/org/acme/extra/ExtraMailResource.java)
* Template: [template](src/main/resources/templates/ExtraMailResource/hello.html)  
* Command: `curl -v http://localhost:8080/extra/template`

### Using type-safe template

* Code: [using type safe template](src/main/java/org/acme/extra/TypeSafeMailResource.java)
* Template: [template](src/main/resources/templates/TypeSafeMailResource/hello.html)
* Command: `curl -v http://localhost:8080/type-safe`



