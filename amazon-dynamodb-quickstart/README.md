# Quarkus demo: Amazon DynamoDB Client

This example showcases how to use the AWS DynamoDB client with Quarkus.

## Run the Demo in Dev Mode

- Run `./mvnw clean quarkus:dev`

Go to [`http://localhost:8080/fruits.html`](http://localhost:8080/fruits.html), it should show a simple App to manage a list of Fruits. 
You can add fruits to the list via the form.

Alternatively, go to [`http://localhost:8080/async-fruits.html`](http://localhost:8080/async-fruits.html) with the simple App communicating with Async resources.

# Using LocalStack

As a prerequisite, install the [AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

Start LocalStack:

 ```sh
 docker run \
  --rm \
  --name local-dynamodb \
  -p 4566:4566 \
  locals

DynamoDB listens on `localhost:4566` for REST endpoints.

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

## Create a DynamoDB table

Create a DynamoDB table using AWS CLI and the localstack profile.

```sh
aws dynamodb create-table \
    --table-name QuarkusFruits \
    --attribute-definitions AttributeName=fruitName,AttributeType=S \
    --key-schema AttributeName=fruitName,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
    --profile localstack --endpoint-url=http://localhost:4566`
```

## Run the demo

You can compile the application and run it with:

```sh
./mvnw install
AWS_PROFILE=localstack java -Dquarkus.dynamodb.endpoint-override=http://localhost:4566 -jar ./target/quarkus-app/quarkus-run.jar
```

Go to [`http://localhost:8080/fruits.html`](http://localhost:8080/fruits.html) or [`http://localhost:8080/async-fruits.html`](http://localhost:8080/async-fruits.html), to test the application.

## Running in native

You can compile the application into a native executable using:

```sh
./mvnw install -Dnative
```

And run it with:

```sh
AWS_PROFILE=localstack ./target/amazon-dynamodb-quickstart-1.0.0-SNAPSHOT-runner -Dquarkus.dynamodb.endpoint-override=http://localhost:4566
```

# Running native in container

Build a native image in a container by running:

```sh
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
```

Build a Docker image:

```sh
docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-dynamodb-quickstart .
```

Create a network that connects your container with LocalStack:

```sh
docker network create localstack
```

Stop your LocalStack container you started at the beginning:

```sh
docker stop local-dynamodb
```

Start LocalStack and connect to the network:

```sh
docker run \
  --rm \
  --name local-dynamodb \
  --network=localstack \
  -p 4566:4566 \
  localstack/localstack
```

Create a DynamoDB table using AWS CLI and the localstack profile.

```sh
aws dynamodb create-table \
    --table-name QuarkusFruits \
    --attribute-definitions AttributeName=fruitName,AttributeType=S \
    --key-schema AttributeName=fruitName,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
    --profile localstack --endpoint-url=http://localhost:4566`
```

Run the Quickstart container connected to that network (note that we're using the internal port of the LocalStack container):

```sh
docker run -i --rm --network=localstack \
  -p 8080:8080 \
  -e QUARKUS_DYNAMODB_ENDPOINT_OVERRIDE="http://local-dynamodb:4566" \
  -e QUARKUS_DYNAMODB_AWS_REGION="us-east-1" \
  -e QUARKUS_DYNAMODB_AWS_CREDENTIALS_TYPE="static" \
  -e QUARKUS_DYNAMODB_AWS_CREDENTIALS_STATIC_PROVIDER_ACCESS_KEY_ID="test-key" \
  -e QUARKUS_DYNAMODB_AWS_CREDENTIALS_STATIC_PROVIDER_SECRET_ACCESS_KEY="test-secret" \
  quarkus/amazon-dynamodb-quickstart
```

Go to `http://localhost:8080/dynamodb.html` or `http://localhost:8080/async-dynamodb.html`, to test the application.

Clean up your environment:

```sh
docker stop local-dynamodb
docker network rm localstack
```

# Using AWS account

Before you can use the AWS SDKs with DynamoDB, you must get an AWS access key ID and secret access key.
For more information, see:
 - [Sign up for AWS and Create an IAM User](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/signup-create-iam-user.html)
 - [Set Up AWS Credentials and Region for Development](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html)

Create a DynamoDB table using AWS CLI and the localstack profile.

```sh
aws dynamodb create-table \
    --table-name QuarkusFruits \
    --attribute-definitions AttributeName=fruitName,AttributeType=S \
    --key-schema AttributeName=fruitName,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1
```
## Run demo

You can run the demo the same way as for a local instance, but you don't need to override the endpoint as you are going to communicate with the AWS service with the default AWS profile.

Run it:

```sh
java -Dbucket.name=quarkus.dynamodb.12.345.99 -jar ./target/quarkus-app/quarkus-run.jar
```

Or, run it natively:

```sh
./target/amazon-dynamodb-quickstart-1.0.0-SNAPSHOT-runner -Dbucket.name=quarkus.dynamodb.12.345.99
```
