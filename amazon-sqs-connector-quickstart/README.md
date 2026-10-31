Quarkus Messaging Amazon SQS Connector Quickstart
============================

This project illustrates how you can interact with AWS SQS using Quarkus Messaging.

## Start the application in dev mode

Run:

```bash
./mvnw quarkus:dev
```

Dev Services starts a Floci container and creates the `quotes` and `quote-requests` queues automatically, so no AWS account is required.

Then, open your browser to `http://localhost:8080/quotes.html`, and click on the "Request Quote" button.

# Run with a local Floci

As a prerequisite, install Docker.
The commands below use the AWS CLI shipped in the Floci `latest-compat` image, so you do not need the AWS CLI on your host.

Start Floci:

```sh
docker run --rm --name floci -p 4566:4566 -d floci/floci:latest-compat
```

SQS listens on `localhost:4566` for REST endpoints.

## Create SQS queues

Create the two queues used by the application:

```sh
docker exec floci aws sqs create-queue --queue-name=quotes
docker exec floci aws sqs create-queue --queue-name=quote-requests
```

## Run the demo

You can compile the application and run it with:

```sh
./mvnw install
java \
  -Dquarkus.sqs.endpoint-override=http://localhost:4566 \
  -Dquarkus.sqs.aws.region=us-east-1 \
  -Dquarkus.sqs.aws.credentials.type=static \
  -Dquarkus.sqs.aws.credentials.static-provider.access-key-id=test-key \
  -Dquarkus.sqs.aws.credentials.static-provider.secret-access-key=test-secret \
  -jar ./target/quarkus-app/quarkus-run.jar
```

Then, open your browser to `http://localhost:8080/quotes.html`, and click on the "Request Quote" button.

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
./target/amazon-sqs-connector-quickstart-1.0.0-SNAPSHOT-runner \
  -Dquarkus.sqs.endpoint-override=http://localhost:4566 \
  -Dquarkus.sqs.aws.region=us-east-1 \
  -Dquarkus.sqs.aws.credentials.type=static \
  -Dquarkus.sqs.aws.credentials.static-provider.access-key-id=test-key \
  -Dquarkus.sqs.aws.credentials.static-provider.secret-access-key=test-secret
```

# Run the app and Floci in containers

Build a native image in a container by running:

```sh
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
```

Build a Docker image:

```sh
docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-sqs-connector-quickstart .
```

Create a network that connects your container with Floci, and start Floci on it:

```sh
docker network create floci
docker run --rm --name floci --network=floci -p 4566:4566 -d floci/floci:latest-compat
```

Create the queues:

```sh
docker exec floci aws sqs create-queue --queue-name=quotes
docker exec floci aws sqs create-queue --queue-name=quote-requests
```

Run the Quickstart container connected to that network (note that the application uses the internal Floci URL, `http://floci:4566`):

```sh
docker run -i --rm --network=floci \
  -p 8080:8080 \
  -e QUARKUS_SQS_ENDPOINT_OVERRIDE="http://floci:4566" \
  -e QUARKUS_SQS_AWS_REGION="us-east-1" \
  -e QUARKUS_SQS_AWS_CREDENTIALS_TYPE="static" \
  -e QUARKUS_SQS_AWS_CREDENTIALS_STATIC_PROVIDER_ACCESS_KEY_ID="test-key" \
  -e QUARKUS_SQS_AWS_CREDENTIALS_STATIC_PROVIDER_SECRET_ACCESS_KEY="test-secret" \
  quarkus/amazon-sqs-connector-quickstart
```

Then, open your browser to `http://localhost:8080/quotes.html`, and click on the "Request Quote" button.

Clean up your environment:

```sh
docker stop floci
docker network rm floci
```

See the [Packaging applications](https://quarkus.io/guides/packaging) guide for more details.

# Using AWS account

As a prerequisite, install the [AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

Before you can use the AWS SDKs with SQS, you must get an AWS access key ID and secret access key.
For more information, see:
- [Sign up for AWS and Create an IAM User](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/signup-create-iam-user.html)
- [Set Up AWS Credentials and Region for Development](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html)

Create the two queues:

```sh
aws sqs create-queue --queue-name=quotes
aws sqs create-queue --queue-name=quote-requests
```

## Run demo

You can run the demo the same way as for a local instance, but you don't need to override the endpoint as you are going to communicate with the AWS service with the default AWS profile.

Run it:

```sh
java -jar ./target/quarkus-app/quarkus-run.jar
```

Or, run it natively:

```sh
./target/amazon-sqs-connector-quickstart-1.0.0-SNAPSHOT-runner
```
