# Using Keycloak Authorization Services and Policy Enforcer to Protect JAX-RS Applications

In this example, we build a very simple microservice which offers two endpoints:

* `/api/users/me`
* `/api/admin`

These endpoints are protected and can only be accessed if a client is sending a bearer token along with the request, which must be valid (e.g.: signature, expiration and audience) and trusted by the microservice.

The bearer token is issued by a Keycloak Server and represents the subject to which the token was issued for.
For being an OAuth 2.0 Authorization Server, the token also references the client acting on behalf of the user.

The `/api/users/me` endpoint can be accessed by any user with a valid token.
As a response, it returns a JSON document with details about the user where these details are obtained from the information carried on the token.
This endpoint is protected with RBAC (Role-Based Access Control) and only users granted with the `user` role can access this endpoint.

The `/api/admin` endpoint is protected with RBAC (Role-Based Access Control) and only users granted with the `admin` role can access it.

This is a very simple example using RBAC policies to govern access to your resources.
However, Keycloak supports other types of policies that you can use to perform even more fine-grained access control.
By using this example, you'll see that your application is completely decoupled from your authorization policies with enforcement being purely based on the accessed resource.

## Requirements

To compile and run this demo you will need:

- JDK 11+
- GraalVM
- Keycloak

### Configuring GraalVM and JDK 11+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 11+ `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image)
for help setting up your environment.

## Building the application

Launch the Maven build on the checked out sources of this demo:
```bash
 ./mvnw package
```
## Starting and Configuring the Keycloak Server

> :warning: **NOTE**: Do not start the Keycloak server when you run the application in a dev mode - `Dev Services for Keycloak` will launch a container for you.

To start a Keycloak Server you can use Docker and just run the following command in the root directory of this quickstart:

```bash
docker run --name keycloak -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -p 8543:8443 -v "$(pwd)"/config/keycloak-keystore.jks:/etc/keycloak-keystore.jks quay.io/keycloak/keycloak:{keycloak.version} start  --hostname-strict=false --https-key-store-file=/etc/keycloak-keystore.jks
```

where `keycloak.version` should be set to `17.0.0` or higher.

You should be able to access your Keycloak Server at https://localhost:8543.

Log in as the `admin` user to access the Keycloak Administration Console.
Username should be `admin` and password `admin`.

Import the [realm configuration file](config/quarkus-realm.json) to create a new realm.
For more details, see the Keycloak documentation about how to [create a new realm](https://www.keycloak.org/docs/latest/server_admin/index.html#_create-realm).

### Live coding with Quarkus

The Maven Quarkus plugin provides a development mode that supports
live coding. To try this out:
```bash
./mvnw quarkus:dev
```
This command will leave Quarkus running in the foreground listening on port 8080.

Now open [OpenId Connect Dev UI](http://localhost:8080/q/dev). You will be asked to login into a _Single Page Application_. Log in as `alice:alice` - accessing the `/api/admin` will return `403` and `/api/users/me` - `200` as `alice` only has a _User Permission_ to access the `/api/users/me` resource. Logout and login as `admin:admin` - accessing both `/api/admin` and `/api/users/me` will return `200` since `admin` has both _Admin Permission_ to access the `/api/admin` resource and _User Permission_ to access the `/api/users/me` resource.

### Run Quarkus in JVM mode

When you're done iterating in developer mode, you can run the application as a
conventional jar file. First compile it:
```bash
./mvnw package
```
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

> ./target/security-keycloak-authorization-quickstart-1.0.0-SNAPSHOT-runner

### Testing the application

See the `Live coding with Quarkus` section above about testing your application in a dev mode.

You can test the application launched in JVM or Native modes with `curl`.

The application is using bearer token authorization and the first thing to do is obtain an access token from the Keycloak Server in
order to access the application resources:

```bash
export access_token=$(\
    curl --insecure -X POST https://localhost:8543/realms/quarkus/protocol/openid-connect/token \
    --user backend-service:secret \
    -H 'content-type: application/x-www-form-urlencoded' \
    -d 'username=alice&password=alice&grant_type=password' | jq --raw-output '.access_token' \
 )
```

The example above obtains an access token for user `alice`.

Any user is allowed to access the
`http://localhost:8080/api/users/me` endpoint
which basically returns a JSON payload with details about the user.

```bash
curl -v -X GET \
  http://localhost:8080/api/users/me \
  -H "Authorization: Bearer "$access_token
```

The `http://localhost:8080/api/admin` endpoint can only be accessed by users with the `admin` role. If you try to access this endpoint with the
 previously issued access token, you should get a `403` response
 from the server.

```bash
curl -v -X GET \
   http://localhost:8080/api/admin \
   -H "Authorization: Bearer "$access_token
```

In order to access the admin endpoint you should obtain a token for the `admin` user:

```bash
export access_token=$(\
    curl --insecure -X POST https://localhost:8543/realms/quarkus/protocol/openid-connect/token \
    --user backend-service:secret \
    -H 'content-type: application/x-www-form-urlencoded' \
    -d 'username=admin&password=admin&grant_type=password' | jq --raw-output '.access_token' \
 )
```

Please also note the integration tests depend on `Dev Services for Keycloak`.
