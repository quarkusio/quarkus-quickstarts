# Quarkus Demo: Amazon SQS Client

This example showcases how to use the AWS SQS client with Quarkus.

## Run the Demo in Dev Mode

- Run `./mvnw clean quarkus:dev`

## Send messages to the queue

Shoot with a couple of quarks:

```sh
curl -XPOST -H "Content-type: application/json" http://localhost:8080/sync/cannon/shoot -d '{"flavor": "Charm", "spin": "1/2"}'
curl -XPOST -H "Content-type: application/json" http://localhost:8080/sync/cannon/shoot -d '{"flavor": "Strange", "spin": "1/2"}'
```

And receive them from the queue:

```sh
curl http://localhost:8080/sync/shield
```

## Test the async endpoints

Replace `sync` with `async` in the examples above to test the asynchronous endpoints.

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
aws sqs create-queue --queue-name=Quarkus --profile localstack --endpoint-url=http://localhost:4566
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
AWS_PROFILE=localstack ./target/amazon-sqs-quickstart-1.0.0-SNAPSHOT-runner -Dquarkus.sqs.endpoint-override=http://localhost:4566
```

# Running native in container

Build a native image in a container by running:

```sh
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
```

Build a Docker image:

```sh
docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-sqs-quickstart .
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
aws sqs create-queue --queue-name=Quarkus --profile localstack --endpoint-url=http://localhost:4566
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
  quarkus/amazon-sqs-quickstart
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
QUEUE_URL=`aws sqs create-queue --queue-name=ColliderQueue`
```

## Run demo

You can run the demo the same way as for a local instance, but you don't need to override the endpoint as you are going to communicate with the AWS service with the default AWS profile.

Run it:

```sh
java -Dqueue.url=$QUEUE_URL -jar ./target/quarkus-app/quarkus-run.jar
```

Or, run it natively:

```sh
./target/amazon-sqs-quickstart-1.0.0-SNAPSHOT-runner -Dqueue.url=$QUEUE_URL
```
