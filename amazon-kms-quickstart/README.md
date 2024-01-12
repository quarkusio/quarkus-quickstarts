# Quarkus demo: Amazon KMS Client

This example showcases how to use the AWS KMS client with Quarkus.

## Run the Demo in Dev Mode

- Run `./mvnw clean quarkus:dev`

## Encrypt your text

```sh
curl -XPOST -H"Content-type: text/plain" http://localhost:8080/sync/encrypt -d'Quarkus is awsome'
```

And the result similar to this output:

```plain
S2Fybjphd3M6a21zOnVzLWVhc3QtMTowMDAwMDAwMDAwMDA6a2V5LzZmYzAwOWZhLWYwMDUtNGI4My04ZDc1LTk4OGVhZTk4ZTM1NwAAAAAfC2HyHrHBXLNFomHLdH9PlMKWQKofyhJjbY2TUovEaBuc4Hj+Lb2BSoYTa/c=
```

## Decrypt your message

You can now decrypt a message you previously encrypted

```sh
curl -XPOST -H"Content-type: text/plain" http://localhost:8080/sync/decrypt -d '<encrypted-message>'
```

Repeat the same using async endpoints. Encrypt

```sh
curl -XPOST -H"Content-type: text/plain" http://localhost:8080/async/encrypt -d 'Quarkus is awsome'
```

And then decrypt

```sh
curl -XPOST -H"Content-type: text/plain" http://localhost:8080/async/decrypt -d '<encrypted-message>'
```

# Using LocalStack

As a prerequisite, install the [AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

Start LocalStack:

 ```sh
 docker run \
  --rm \
  --name local-kms \
  -p 4566:4566 \
  localstack/localstack
```

KMS listens on `localhost:4566` for REST endpoints.

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

## Create KMS master key

Create a master key with an alias for simplier configuration.

```sh
key_id=$(aws kms create-key --query KeyMetadata.KeyId --output text  --profile localstack --endpoint-url=http://localhost:4566)
aws kms create-alias --alias-name "alias/quarkus" --target-key-id $key_id  --profile localstack --endpoint-url=http://localhost:4566
```

## Run the demo

You can compile the application and run it with:

```sh
./mvnw install
AWS_PROFILE=localstack java -Dquarkus.kms.endpoint-override=http://localhost:4566 -jar ./target/quarkus-app/quarkus-run.jar
```

You can now replay the `curl` commands above.

## Running in native

You can compile the application into a native executable using:

```sh
./mvnw install -Dnative
```

And run it with:

```sh
AWS_PROFILE=localstack ./target/amazon-kms-quickstart-1.0.0-SNAPSHOT-runner -Dquarkus.kms.endpoint-override=http://localhost:4566
```

# Running native in container

Build a native image in a container by running:

```sh
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
```

Build a Docker image:

```sh
docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-kms-quickstart .
```

Create a network that connects your container with LocalStack:

```sh
docker network create localstack
```

Stop your LocalStack container you started at the beginning:

```sh
docker stop local-kms
```

Start LocalStack and connect to the network:

```sh
docker run \
  --rm \
  --name local-kms \
  --network=localstack \
  -p 4566:4566 \
  localstack/localstack
```

Create a master key with an alias for simplier configuration.

```sh
key_id=$(aws kms create-key --query KeyMetadata.KeyId --output text  --profile localstack --endpoint-url=http://localhost:4566)
aws kms create-alias --alias-name "alias/quarkus" --target-key-id $key_id  --profile localstack --endpoint-url=http://localhost:4566
```

Run the Quickstart container connected to that network (note that we're using the internal port of the LocalStack container):

```sh
docker run -i --rm --network=localstack \
  -p 8080:8080 \
  -e QUARKUS_KMS_ENDPOINT_OVERRIDE="http://local-kms:4566" \
  -e QUARKUS_KMS_AWS_REGION="us-east-1" \
  -e QUARKUS_KMS_AWS_CREDENTIALS_TYPE="static" \
  -e QUARKUS_KMS_AWS_CREDENTIALS_STATIC_PROVIDER_ACCESS_KEY_ID="test-key" \
  -e QUARKUS_KMS_AWS_CREDENTIALS_STATIC_PROVIDER_SECRET_ACCESS_KEY="test-secret" \
  quarkus/amazon-kms-quickstart
```

Replay `curl` commands from above:

Clean up your environment:

```sh
docker stop local-kms
docker network rm localstack
```

# Using AWS account

Before you can use the AWS SDKs with KMS, you must get an AWS access key ID and secret access key.
For more information, see:
 - [Sign up for AWS and Create an IAM User](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/signup-create-iam-user.html)
 - [Set Up AWS Credentials and Region for Development](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html)

Create a master key with an alias for simplier configuration.

```sh
key_id=$(aws kms create-key --query KeyMetadata.KeyId --output text)
aws kms create-alias --alias-name "alias/quarkus" --target-key-id $key_id
```

## Run demo

You can run the demo the same way as for a local instance, but you don't need to override the endpoint as you are going to communicate with the AWS service with the default AWS profile.

Run it:

```sh
java -jar ./target/quarkus-app/quarkus-run.jar
```

Or, run it natively:

```sh
./target/amazon-kms-quickstart-1.0.0-SNAPSHOT-runner
```
