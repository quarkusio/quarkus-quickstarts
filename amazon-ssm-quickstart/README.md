# Quarkus demo: Amazon SSM Client

This example showcases how to use the AWS SSM client with Quarkus.

## Run the demo in dev mode

- Run `./mvnw clean quarkus:dev`

Dev Services starts a Floci container, so no AWS account is required.

## Set some parameters

First, add as many parameters as you like using the following patterns for secure and plain parameters:

```sh
curl -XPUT -H"Content-type: text/plain" "http://localhost:8080/sync/secure?secure=true" -d"stored as cipher text"
curl -XPUT -H"Content-type: text/plain" "http://localhost:8080/sync/plain" -d"stored as plain text"
```

## List all parameters

You can now list the parameters you added:

```sh
curl http://localhost:8080/sync
```

You should see output like this:

```plain
{"plain":"stored as plain text","secure":"stored as cipher text"}
```

## Test the async endpoints

Replace `sync` with `async` in the examples above to test the asynchronous endpoints.

# Run with a local Floci

As a prerequisite, install Docker.
The commands below use the AWS CLI shipped in the Floci `latest-compat` image, so you do not need the AWS CLI on your host.

Start Floci:

```sh
docker run --rm --name floci -p 4566:4566 -d floci/floci:latest-compat
```

SSM listens on `localhost:4566` for REST endpoints.
No parameter needs to be created up front: the application creates them through the REST API.

## Run the demo

You can compile the application and run it with:

```sh
./mvnw install
java \
  -Dquarkus.ssm.endpoint-override=http://localhost:4566 \
  -Dquarkus.ssm.aws.region=us-east-1 \
  -Dquarkus.ssm.aws.credentials.type=static \
  -Dquarkus.ssm.aws.credentials.static-provider.access-key-id=test-key \
  -Dquarkus.ssm.aws.credentials.static-provider.secret-access-key=test-secret \
  -jar ./target/quarkus-app/quarkus-run.jar
```

You can now replay the `curl` commands above.

## Running in native

You can compile the application into a native executable using:

```sh
./mvnw install -Dnative
```

Without GraalVM or Mandrel installed, build the native executable inside a container:

```sh
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
```

And run it with:

```sh
./target/amazon-ssm-quickstart-1.0.0-SNAPSHOT-runner \
  -Dquarkus.ssm.endpoint-override=http://localhost:4566 \
  -Dquarkus.ssm.aws.region=us-east-1 \
  -Dquarkus.ssm.aws.credentials.type=static \
  -Dquarkus.ssm.aws.credentials.static-provider.access-key-id=test-key \
  -Dquarkus.ssm.aws.credentials.static-provider.secret-access-key=test-secret
```

# Run the app and Floci in containers

Build a native image in a container by running:

```sh
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
```

Build a Docker image:

```sh
docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-ssm-quickstart .
```

Create a network that connects your container with Floci, and start Floci on it:

```sh
docker network create floci
docker run --rm --name floci --network=floci -p 4566:4566 -d floci/floci:latest-compat
```

Run the Quickstart container connected to that network (note that the application uses the internal Floci URL, `http://floci:4566`):

```sh
docker run -i --rm --network=floci \
  -p 8080:8080 \
  -e QUARKUS_SSM_ENDPOINT_OVERRIDE="http://floci:4566" \
  -e QUARKUS_SSM_AWS_REGION="us-east-1" \
  -e QUARKUS_SSM_AWS_CREDENTIALS_TYPE="static" \
  -e QUARKUS_SSM_AWS_CREDENTIALS_STATIC_PROVIDER_ACCESS_KEY_ID="test-key" \
  -e QUARKUS_SSM_AWS_CREDENTIALS_STATIC_PROVIDER_SECRET_ACCESS_KEY="test-secret" \
  quarkus/amazon-ssm-quickstart
```

You can now replay the `curl` commands above.

Clean up your environment:

```sh
docker stop floci
docker network rm floci
```

See the [Packaging applications](https://quarkus.io/guides/packaging) guide for more details.

# Using AWS account

As a prerequisite, install the [AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

Before you can use the AWS SDKs with SSM, you must get an AWS access key ID and secret access key.
For more information, see:
 - [Sign up for AWS and Create an IAM User](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/signup-create-iam-user.html)
 - [Set Up AWS Credentials and Region for Development](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html)

## Run demo

You can run the demo the same way as for a local instance, but you don't need to override the endpoint as you are going to communicate with the AWS service with the default AWS profile.

Run it:

```sh
java -jar ./target/quarkus-app/quarkus-run.jar
```

Or, run it natively:

```sh
./target/amazon-ssm-quickstart-1.0.0-SNAPSHOT-runner
```
