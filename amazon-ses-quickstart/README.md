# Quarkus demo: Amazon SES Client

This example showcases how to use the AWS SES client with Quarkus.
Despite the fact that this example uses a local AWS SES for integration test purposes, we encourage you to use SES from an AWS account as it allows you to send emails.
A local instance of SES only mocks the service APIs and doesn't send any emails.

## Run the Demo in Dev Mode

- Run `./mvnw clean quarkus:dev`

Dev Services starts a Floci container and verifies the sender address (`from-quarkus@example.com`) automatically, so no AWS account is required.

## Send an email

Using sync endpoint

```sh
curl -XPOST -H"Content-type: application/json" http://localhost:8080/sync/email -d'{"from": "from-quarkus@example.com", "to": "to-quarkus@example.com", "subject": "Hello from Quarkus", "body": "Quarkus is awesome"}'
```

Or async endpoint

```sh
curl -XPOST -H"Content-type: application/json" http://localhost:8080/async/email -d'{"from": "from-quarkus@example.com", "to": "to-quarkus@example.com", "subject": "Hello from Quarkus", "body": "Quarkus is awesome"}'
```

As a result, you will see the ID of the message as SES returned. E.g.:

```plain
010701724bec5607-e34882d5-a8ce-4f2b-a837-da75989e43c0-000000
```

# Run with a local Floci

As a prerequisite, install Docker.
The commands below use the AWS CLI shipped in the Floci `latest-compat` image, so you do not need the AWS CLI on your host.

Start Floci:

```sh
docker run --rm --name floci -p 4566:4566 -d floci/floci:latest-compat
```

SES listens on `localhost:4566` for REST endpoints.

## Verify the sender email address

Floci rejects a send request when the sender address is not verified.
Only the sender must be verified; the recipient does not have to be, and no email is actually sent.

```sh
docker exec floci aws ses verify-email-identity --email-address from-quarkus@example.com
```

## Run the demo

You can compile the application and run it with:

```sh
./mvnw install
java \
  -Dquarkus.ses.endpoint-override=http://localhost:4566 \
  -Dquarkus.ses.aws.region=us-east-1 \
  -Dquarkus.ses.aws.credentials.type=static \
  -Dquarkus.ses.aws.credentials.static-provider.access-key-id=test-key \
  -Dquarkus.ses.aws.credentials.static-provider.secret-access-key=test-secret \
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
./target/amazon-ses-quickstart-1.0.0-SNAPSHOT-runner \
  -Dquarkus.ses.endpoint-override=http://localhost:4566 \
  -Dquarkus.ses.aws.region=us-east-1 \
  -Dquarkus.ses.aws.credentials.type=static \
  -Dquarkus.ses.aws.credentials.static-provider.access-key-id=test-key \
  -Dquarkus.ses.aws.credentials.static-provider.secret-access-key=test-secret
```

# Run the app and Floci in containers

Build a native image in a container by running:

```sh
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
```

Build a Docker image:

```sh
docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-ses-quickstart .
```

Create a network that connects your container with Floci, and start Floci on it:

```sh
docker network create floci
docker run --rm --name floci --network=floci -p 4566:4566 -d floci/floci:latest-compat
```

Verify the sender email address:

```sh
docker exec floci aws ses verify-email-identity --email-address from-quarkus@example.com
```

Run the Quickstart container connected to that network (note that the application uses the internal Floci URL, `http://floci:4566`):

```sh
docker run -i --rm --network=floci \
  -p 8080:8080 \
  -e QUARKUS_SES_ENDPOINT_OVERRIDE="http://floci:4566" \
  -e QUARKUS_SES_AWS_REGION="us-east-1" \
  -e QUARKUS_SES_AWS_CREDENTIALS_TYPE="static" \
  -e QUARKUS_SES_AWS_CREDENTIALS_STATIC_PROVIDER_ACCESS_KEY_ID="test-key" \
  -e QUARKUS_SES_AWS_CREDENTIALS_STATIC_PROVIDER_SECRET_ACCESS_KEY="test-secret" \
  quarkus/amazon-ses-quickstart
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

Before you can use the AWS SDKs with SES, you must get an AWS access key ID and secret access key.
For more information, see:
 - [Sign up for AWS and Create an IAM User](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/signup-create-iam-user.html)
 - [Set Up AWS Credentials and Region for Development](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html)

Verify the sender and the recipient email addresses:

```sh
aws ses verify-email-identity --email-address from-quarkus@example.com
aws ses verify-email-identity --email-address to-quarkus@example.com
```

On AWS, verifying email identities or domain identities requires additional steps like changing DNS configuration or clicking verification links respectively. Use an email address that you can verify.

## Run demo

You can run the demo the same way as for a local instance, but you don't need to override the endpoint as you are going to communicate with the AWS service with the default AWS profile.

Run it:

```sh
java -jar ./target/quarkus-app/quarkus-run.jar
```

Or, run it natively:

```sh
./target/amazon-ses-quickstart-1.0.0-SNAPSHOT-runner
```
