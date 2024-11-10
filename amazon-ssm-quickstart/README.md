# Quarkus demo: Amazon SSM Client

This example showcases how to use the AWS SSM client with Quarkus.

# Run the demo on dev mode

- Run `./mvnw clean quarkus:dev`

## Set some parameters

First, add as many paramters as you like using the following patterns for secure and plain parameters:

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

# Using LocalStack

As a prerequisite, install the [AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

Start LocalStack:

 ```sh
 docker run \
  --rm \
  --name local-ssm \
  -p 4566:4566 \
  localstack/localstack
```

SSM listens on `localhost:4566` for REST endpoints.

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

## Run the demo

You can compile the application and run it with:

```sh
./mvnw install
AWS_PROFILE=localstack java -Dquarkus.ssm.endpoint-override=http://localhost:4566 -jar ./target/quarkus-app/quarkus-run.jar
```

## Running in native

You can compile the application into a native executable using:

```sh
./mvnw install -Dnative
```

And run it with:

```sh
AWS_PROFILE=localstack ./target/amazon-ssm-quickstart-1.0.0-SNAPSHOT-runner -Dquarkus.ssm.endpoint-override=http://localhost:4566
```

## Running native in container

Build a native image in a container by running:

```sh
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
```

Build a Docker image:

```sh
docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-ssm-quickstart .
```

Create a network that connects your container with LocalStack:

```sh
docker network create localstack
```

Stop your LocalStack container you started at the beginning:

```sh
docker stop local-ssm
```

Start LocalStack and connect to the network:

```sh
docker run \
  --rm \
  --name local-ssm \
  --network=localstack \
  -p 4566:4566 \
  localstack/localstack
```

Run the Quickstart container connected to that network (note that we're using the internal port of the LocalStack container):

```sh
docker run -i --rm --network=localstack \
  -p 8080:8080 \
  -e QUARKUS_SSM_ENDPOINT_OVERRIDE="http://local-ssm:4566" \
  -e QUARKUS_SSM_AWS_REGION="us-east-1" \
  -e QUARKUS_SSM_AWS_CREDENTIALS_TYPE="static" \
  -e QUARKUS_SSM_AWS_CREDENTIALS_STATIC_PROVIDER_ACCESS_KEY_ID="test-key" \
  -e QUARKUS_SSM_AWS_CREDENTIALS_STATIC_PROVIDER_SECRET_ACCESS_KEY="test-secret" \
  quarkus/amazon-ssm-quickstart
```

You can now replay the `curl` commands above.

Clean up your environment:

```sh
docker stop local-ssm
docker network rm localstack
```

# Using AWS account

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
