# Quarkus demo: Amazon DynamoDB Client

This example showcases how to use the AWS DynamoDB client with Quarkus.

## Run the Demo in Dev Mode

- Run `./mvnw clean quarkus:dev`

Dev Services starts a Floci container and creates the `QuarkusFruits` table automatically, so no AWS account is required.

Go to [`http://localhost:8080/fruits.html`](http://localhost:8080/fruits.html), it should show a simple App to manage a list of Fruits.
You can add fruits to the list via the form.

Alternatively, go to [`http://localhost:8080/async-fruits.html`](http://localhost:8080/async-fruits.html) with the simple App communicating with Async resources.

# Run with a local Floci

As a prerequisite, install Docker.
The commands below use the AWS CLI shipped in the Floci `latest-compat` image, so you do not need the AWS CLI on your host.

Start Floci:

```sh
docker run --rm --name floci -p 4566:4566 -d floci/floci:latest-compat
```

DynamoDB listens on `localhost:4566` for REST endpoints.

## Create the table

All the fruits are stored under the same `partitionKey` value and sorted by their name with the `fruitName` sort key.

```sh
docker exec floci aws dynamodb create-table --table-name QuarkusFruits \
    --attribute-definitions AttributeName=partitionKey,AttributeType=S \
                            AttributeName=fruitName,AttributeType=S \
    --key-schema AttributeName=partitionKey,KeyType=HASH \
                 AttributeName=fruitName,KeyType=RANGE \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1
```

## Run the demo

You can compile the application and run it with:

```sh
./mvnw install
java \
  -Dquarkus.dynamodb.endpoint-override=http://localhost:4566 \
  -Dquarkus.dynamodb.aws.region=us-east-1 \
  -Dquarkus.dynamodb.aws.credentials.type=static \
  -Dquarkus.dynamodb.aws.credentials.static-provider.access-key-id=test-key \
  -Dquarkus.dynamodb.aws.credentials.static-provider.secret-access-key=test-secret \
  -jar ./target/quarkus-app/quarkus-run.jar
```

Go to [`http://localhost:8080/fruits.html`](http://localhost:8080/fruits.html) or [`http://localhost:8080/async-fruits.html`](http://localhost:8080/async-fruits.html), to test the application.

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
./target/amazon-dynamodb-quickstart-1.0.0-SNAPSHOT-runner \
  -Dquarkus.dynamodb.endpoint-override=http://localhost:4566 \
  -Dquarkus.dynamodb.aws.region=us-east-1 \
  -Dquarkus.dynamodb.aws.credentials.type=static \
  -Dquarkus.dynamodb.aws.credentials.static-provider.access-key-id=test-key \
  -Dquarkus.dynamodb.aws.credentials.static-provider.secret-access-key=test-secret
```

# Run the app and Floci in containers

Build a native image in a container by running:

```sh
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
```

Build a Docker image:

```sh
docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-dynamodb-quickstart .
```

Create a network that connects your container with Floci, and start Floci on it:

```sh
docker network create floci
docker run --rm --name floci --network=floci -p 4566:4566 -d floci/floci:latest-compat
```

Create the table:

```sh
docker exec floci aws dynamodb create-table --table-name QuarkusFruits \
    --attribute-definitions AttributeName=partitionKey,AttributeType=S \
                            AttributeName=fruitName,AttributeType=S \
    --key-schema AttributeName=partitionKey,KeyType=HASH \
                 AttributeName=fruitName,KeyType=RANGE \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1
```

Run the Quickstart container connected to that network (note that the application uses the internal Floci URL, `http://floci:4566`):

```sh
docker run -i --rm --network=floci \
  -p 8080:8080 \
  -e QUARKUS_DYNAMODB_ENDPOINT_OVERRIDE="http://floci:4566" \
  -e QUARKUS_DYNAMODB_AWS_REGION="us-east-1" \
  -e QUARKUS_DYNAMODB_AWS_CREDENTIALS_TYPE="static" \
  -e QUARKUS_DYNAMODB_AWS_CREDENTIALS_STATIC_PROVIDER_ACCESS_KEY_ID="test-key" \
  -e QUARKUS_DYNAMODB_AWS_CREDENTIALS_STATIC_PROVIDER_SECRET_ACCESS_KEY="test-secret" \
  quarkus/amazon-dynamodb-quickstart
```

Go to [`http://localhost:8080/fruits.html`](http://localhost:8080/fruits.html) or [`http://localhost:8080/async-fruits.html`](http://localhost:8080/async-fruits.html), to test the application.

Clean up your environment:

```sh
docker stop floci
docker network rm floci
```

See the [Packaging applications](https://quarkus.io/guides/packaging) guide for more details.

# Using AWS account

As a prerequisite, install the [AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

Before you can use the AWS SDKs with DynamoDB, you must get an AWS access key ID and secret access key.
For more information, see:
 - [Sign up for AWS and Create an IAM User](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/signup-create-iam-user.html)
 - [Set Up AWS Credentials and Region for Development](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html)

Create the table with your own AWS CLI:

```sh
aws dynamodb create-table --table-name QuarkusFruits \
    --attribute-definitions AttributeName=partitionKey,AttributeType=S \
                            AttributeName=fruitName,AttributeType=S \
    --key-schema AttributeName=partitionKey,KeyType=HASH \
                 AttributeName=fruitName,KeyType=RANGE \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1
```

## Run demo

You can run the demo the same way as for a local instance, but you don't need to override the endpoint as you are going to communicate with the AWS service with the default AWS profile.

Run it:

```sh
java -jar ./target/quarkus-app/quarkus-run.jar
```

Or, run it natively:

```sh
./target/amazon-dynamodb-quickstart-1.0.0-SNAPSHOT-runner
```
