Quarkus Messaging Amazon SQS Connector Quickstart
============================

This project illustrates how you can interact with AWS SQS using Quarkus Messaging.

## Start the application in dev mode

In a first terminal, run:

```bash
> mvn quarkus:dev
```

Then, open your browser to `http://localhost:8080/quotes.html`, and click on the "Request Quote" button.

# Using LocalStack

As a prerequisite, install the [AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

Start LocalStack:

 ```sh
 docker run \
  --rm \
  --name local-sqs \
  -p 4566:4566 \
  localstack/localstack
```

SQS listens on `localhost:4566` for REST endpoints.

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

## Create SQS queue

Create a SQS queue:

```sh
aws sqs create-queue --queue-name=quotes --profile localstack --endpoint-url=http://localhost:4566
aws sqs create-queue --queue-name=quote-requests --profile localstack --endpoint-url=http://localhost:4566
```

## Run the demo

You can compile the application and run it with:

```sh
./mvnw install
AWS_PROFILE=localstack java -Dquarkus.sqs.endpoint-override=http://localhost:4566 -jar ./target/quarkus-app/quarkus-run.jar
```

## Running in native

You can compile the application into a native executable using:

```sh
./mvnw install -Dnative
```

And run it with:

```sh
AWS_PROFILE=localstack ./target/amazon-sqs-connector-quickstart-1.0.0-SNAPSHOT-runner -Dquarkus.sqs.endpoint-override=http://localhost:4566
```

# Running native in container

Build a native image in a container by running:

```sh
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
```

Build a Docker image:

```sh
docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-sqs-connector-quickstart .
```

Create a network that connects your container with LocalStack:

```sh
docker network create localstack
```

Stop your LocalStack container you started at the beginning:

```sh
docker stop local-sqs
```

Start LocalStack and connect to the network:

```sh
docker run \
  --rm \
  --name local-sqs \
  --network=localstack \
  -p 4566:4566 \
  localstack/localstack
```

Create a queue:

```sh
aws sqs create-queue --queue-name=quotes --profile localstack --endpoint-url=http://localhost:4566
aws sqs create-queue --queue-name=quote-requests --profile localstack --endpoint-url=http://localhost:4566
```

Run the Quickstart container connected to that network (note that we're using the internal port of the LocalStack container):

```sh
docker run -i --rm --network=localstack \
  -p 8080:8080 \
  -e QUARKUS_SQS_ENDPOINT_OVERRIDE="http://local-sqs:4566" \
  -e QUARKUS_SQS_AWS_REGION="us-east-1" \
  -e QUARKUS_SQS_AWS_CREDENTIALS_TYPE="static" \
  -e QUARKUS_SQS_AWS_CREDENTIALS_STATIC_PROVIDER_ACCESS_KEY_ID="test-key" \
  -e QUARKUS_SQS_AWS_CREDENTIALS_STATIC_PROVIDER_SECRET_ACCESS_KEY="test-secret" \
  quarkus/amazon-sqs-connector-quickstart
```

You can now replay the `curl` commands above.

Clean up your environment:

```sh
docker stop local-sqs
docker network rm localstack
```

# Using AWS account

Before you can use the AWS SDKs with SQS, you must get an AWS access key ID and secret access key.
For more information, see:
- [Sign up for AWS and Create an IAM User](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/signup-create-iam-user.html)
- [Set Up AWS Credentials and Region for Development](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html)

Create a SQS queue and store the queue url in an environment variable as we will need to provide it to the our app:

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
./target/amazon-sqs-quickstart-1.0.0-SNAPSHOT-runner
```
