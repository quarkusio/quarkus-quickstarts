# Quarkus demo: Amazon SES Client

This example showcases how to use the AWS SES client with Quarkus.
Despite the fact, this example uses local AWS SES for integration test purposes, we encourage you to use SES from the AWS account as it allows you to send emails.
Local instance of SES only mocks service APIs and doesn't send any emails.

## Run the Demo in Dev Mode

- Run `./mvnw clean quarkus:dev`

## Send an email

Using sync endpoint

```sh
curl -XPOST -H"Content-type: application/json" http://localhost:8080/sync/email -d'{"from": "from-quarkus@example.com", "to": "to-quarkus@example.com", "subject": "Hello from Quarkus", "body": "Quarkus is awsome"}'
```

Or async endpoint

```sh
curl -XPOST -H"Content-type: application/json" http://localhost:8080/async/email -d'{"from": "from-quarkus@example.com", "to": "to-quarkus@example.com", "subject": "Hello from Quarkus", "body": "Quarkus is awsome"}'
```

As a result, you will see the ID of the message as SES returned. E.g.:

```plain
010701724bec5607-e34882d5-a8ce-4f2b-a837-da75989e43c0-000000
```

# Using LocalStack

As a prerequisite, install the [AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

Start LocalStack:

 ```sh
 docker run \
  --rm \
  --name local-ses \
  -p 4566:4566 \
  localstack/localstack
```

SES listens on `localhost:4566` for REST endpoints.

Create an AWS profile for your local instance using AWS CLI:

```sh
aws configure --profile localstack
```

```plain
AWS Access Key ID [None]: test-key
AWS Secret Access Key [None]: test-secret
Default region name [None]: us-east-1
Default output format [None]:
```

## Verify email addresses

Verify the email addresses you're going to use when running the application.

```sh
aws ses verify-email-identity --email-address from-quarkus@example.com --profile localstack --endpoint-url=http://localhost:4566
```

## Run the demo

You can compile the application and run it with:

```sh
./mvnw install
AWS_PROFILE=localstack java -Dquarkus.ses.endpoint-override=http://localhost:4566 -jar ./target/quarkus-app/quarkus-run.jar
```

You can now replay the `curl` commands above.

## Running in native

You can compile the application into a native executable using:

```sh
./mvnw install -Dnative
```

And run it with:

```sh
AWS_PROFILE=localstack ./target/amazon-ses-quickstart-1.0.0-SNAPSHOT-runner -Dquarkus.ses.endpoint-override=http://localhost:4566
```

# Running native in container

Build a native image in a container by running:

```sh
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
```

Build a Docker image:

```sh
docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-ses-quickstart .
```

Create a network that connects your container with LocalStack:

```sh
docker network create localstack
```

Stop your LocalStack container you started at the beginning:

```sh
docker stop local-ses
```

Start LocalStack and connect to the network:

```sh
docker run \
  --rm \
  --name local-ses \
  --network=localstack \
  -p 4566:4566 \
  localstack/localstack
```

Verify the email addresses:

```sh
aws ses verify-email-identity --email-address from-quarkus@example.com --profile localstack --endpoint-url=http://localhost:4566
```

Run the Quickstart container connected to that network (note that we're using the internal port of the LocalStack container):

```sh
docker run -i --rm --network=localstack \
  -p 8080:8080 \
  -e QUARKUS_SES_ENDPOINT_OVERRIDE="http://local-ses:4566" \
  -e QUARKUS_SES_AWS_REGION="us-east-1" \
  -e QUARKUS_SES_AWS_CREDENTIALS_TYPE="static" \
  -e QUARKUS_SES_AWS_CREDENTIALS_STATIC_PROVIDER_ACCESS_KEY_ID="test-key" \
  -e QUARKUS_SES_AWS_CREDENTIALS_STATIC_PROVIDER_SECRET_ACCESS_KEY="test-secret" \
  quarkus/amazon-ses-quickstart
```

Replay `curl` commands from above:

Clean up your environment:

```sh
docker stop local-ses
docker network rm localstack
```

# Using AWS account

Before you can use the AWS SDKs with SES, you must get an AWS access key ID and secret access key.
For more information, see:
 - [Sign up for AWS and Create an IAM User](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/signup-create-iam-user.html)
 - [Set Up AWS Credentials and Region for Development](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html)

Verify the email addresses:

```sh
aws ses verify-email-identity --email-address from-quarkus@example.com
```

On AWS, verifying email identities or domain identities require additional steps like changing DNS configuration or clicking verification links respectively. Use email address that you can verify.

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
