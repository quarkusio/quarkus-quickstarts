# Getting started with the Quarkus Mailer

This is a minimal application sending emails with Gmail.
It generates the email body with a Qute template.

## Prerequisites

1. You need to generate an application password to use GMail from the application.
Follow these [instructions](https://support.google.com/mail/answer/185833) to create the password. 

2. You need Java 1.8+. However, we recommend Java 11+.
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
If you have the [httpie](https://httpie.io/) command line, you can run:

```shell script
http :8080/mail name==some-name email==target-email-address
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

Then send an email using the same _http_ command. 
