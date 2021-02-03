# Supporting Multi-Tenancy in OpenID Connect Applications

This guide demonstrates how your OpenID Connect application can support multi-tenancy so that you can serve multiple tenants from a single application. Tenants can be distinct realms or security domains within a same OpenID Provider or even distinct OpenID Providers.

When serving multiple customers from a same application (e.g.: SaaS), each customer is a tenant. By enabling multi-tenancy support to your applications you are allowed to also support distinct authentication policies for each tenant even though if that means authenticating against different OpenID Providers, such as Keycloak and Google.

## Requirements

To compile and run this demo you will need:

- JDK 1.8+
- GraalVM
- Keycloak

### Configuring GraalVM and JDK 1.8+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 1.8+ `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image)
for help setting up your environment.

## Building the application

Launch the Maven build on the checked out sources of this demo:

> ./mvnw install

## Starting and Configuring the Keycloak Server

To start a Keycloak Server you can use Docker and just run the following command:

```bash
docker run --name keycloak -e DB_VENDOR=H2 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -p 8180:8080 quay.io/keycloak/keycloak:11.0.2
```

You should be able to access your Keycloak Server at [http://localhost:8180/auth](http://localhost:8180/auth).

Log in as the `admin` user to access the Keycloak Administration Console.
Username should be `admin` and password `admin`.

Now, follow the steps below to important the realms for the two tenants:

* Import the [config/default-tenant-realm.json](config/default-tenant-realm.json) to create the default realm
* Import the [config/tenant-a-realm.json](config/tenant-a-realm.json) to create the realm for the tenant `tenant-a`.

For more details, see the Keycloak documentation about how to [create a new realm](https://www.keycloak.org/docs/latest/server_admin/index.html#_create-realm).

### Live coding with Quarkus

The Maven Quarkus plugin provides a development mode that supports
live coding. To try this out:

> ./mvnw quarkus:dev

This command will leave Quarkus running in the foreground listening on port 8080.

To test the application, you should open your browser and access the following URL:

* [http://localhost:8080/default](http://localhost:8080/default)

If everything is working as expected, you should be redirected to the Keycloak server to authenticate. Note that the requested path
defines a `default` tenant which we don't have mapped in the configuration file. In this case, the default configuration will be used.

In order to authenticate to the application you should type the following credentials when at the Keycloak login page:

* Username: *alice*
* Password: *alice*

After clicking the `Login` button you should be redirected back to the application.

If you try now to access the application at the following URL:

* [http://localhost:8080/tenant-a](http://localhost:8080/tenant-a)

You should be redirect again to the login page at Keycloak. However, now you are going to authenticate using a different `realm`.

In both cases, if the user is successfully authenticated, the landing page will show the user's name and e-mail. Even though the
user `alice` exists in both tenants, for the application they are distinct users belonging to different realms/tenants.

### Run Quarkus in JVM mode

When you're done iterating in developer mode, you can run the application as a
conventional jar file. First compile it:

> ./mvnw install

Then run it:

> java -jar ./target/security-openid-connect-multi-tenancy-quickstart-1.0.0-SNAPSHOT-runner.jar

Have a look at how fast it boots, or measure the total native memory consumption.

### Run Quarkus as a native executable

You can also create a native executable from this application without making any
source code changes. A native executable removes the dependency on the JVM:
everything needed to run the application on the target platform is included in 
the executable, allowing the application to run with minimal resource overhead.

Compiling a native executable takes a bit longer, as GraalVM performs additional
steps to remove unnecessary codepaths. Use the  `native` profile to compile a
native executable:

> ./mvnw install -Dnative

After getting a cup of coffee, you'll be able to run this executable directly:

> ./target/security-openid-connect-multi-tenancy-quickstart-1.0.0-SNAPSHOT-runner
