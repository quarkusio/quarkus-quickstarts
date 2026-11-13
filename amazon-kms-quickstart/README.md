# Quarkus demo: Amazon KMS Client

This example showcases how to use the AWS KMS client with Quarkus.

## Run the Demo in Dev Mode

- Run `./mvnw clean quarkus:dev`

Dev Services starts a Floci container and creates the `alias/quarkus` key automatically, so no AWS account is required.

## Encrypt your text

```sh
curl -XPOST -H"Content-type: text/plain" http://localhost:8080/sync/encrypt -d'Quarkus is awesome'
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
curl -XPOST -H"Content-type: text/plain" http://localhost:8080/async/encrypt -d 'Quarkus is awesome'
```

And then decrypt

```sh
curl -XPOST -H"Content-type: text/plain" http://localhost:8080/async/decrypt -d '<encrypted-message>'
```

# Run with a local Floci

As a prerequisite, install Docker.
The commands below use the AWS CLI shipped in the Floci `latest-compat` image, so you do not need the AWS CLI on your host.

Start Floci:

```sh
docker run --rm --name floci -p 4566:4566 -d floci/floci:latest-compat
```

KMS listens on `localhost:4566` for REST endpoints.

## Create KMS master key

Create a master key with an alias, which the application uses through the `key.arn` property:

```sh
key_id=$(docker exec floci aws kms create-key --query KeyMetadata.KeyId --output text)
docker exec floci aws kms create-alias --alias-name alias/quarkus --target-key-id $key_id
```

## Run the demo

You can compile the application and run it with:

```sh
./mvnw install
java \
  -Dquarkus.kms.endpoint-override=http://localhost:4566 \
  -Dquarkus.kms.aws.region=us-east-1 \
  -Dquarkus.kms.aws.credentials.type=static \
  -Dquarkus.kms.aws.credentials.static-provider.access-key-id=test-key \
  -Dquarkus.kms.aws.credentials.static-provider.secret-access-key=test-secret \
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
./target/amazon-kms-quickstart-1.0.0-SNAPSHOT-runner \
  -Dquarkus.kms.endpoint-override=http://localhost:4566 \
  -Dquarkus.kms.aws.region=us-east-1 \
  -Dquarkus.kms.aws.credentials.type=static \
  -Dquarkus.kms.aws.credentials.static-provider.access-key-id=test-key \
  -Dquarkus.kms.aws.credentials.static-provider.secret-access-key=test-secret
```

# Run the app and Floci in containers

Build a native image in a container by running:

```sh
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
```

Build a Docker image:

```sh
docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-kms-quickstart .
```

Create a network that connects your container with Floci, and start Floci on it:

```sh
docker network create floci
docker run --rm --name floci --network=floci -p 4566:4566 -d floci/floci:latest-compat
```

Create a master key with an alias:

```sh
key_id=$(docker exec floci aws kms create-key --query KeyMetadata.KeyId --output text)
docker exec floci aws kms create-alias --alias-name alias/quarkus --target-key-id $key_id
```

Run the Quickstart container connected to that network (note that the application uses the internal Floci URL, `http://floci:4566`):

```sh
docker run -i --rm --network=floci \
  -p 8080:8080 \
  -e QUARKUS_KMS_ENDPOINT_OVERRIDE="http://floci:4566" \
  -e QUARKUS_KMS_AWS_REGION="us-east-1" \
  -e QUARKUS_KMS_AWS_CREDENTIALS_TYPE="static" \
  -e QUARKUS_KMS_AWS_CREDENTIALS_STATIC_PROVIDER_ACCESS_KEY_ID="test-key" \
  -e QUARKUS_KMS_AWS_CREDENTIALS_STATIC_PROVIDER_SECRET_ACCESS_KEY="test-secret" \
  quarkus/amazon-kms-quickstart
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

Before you can use the AWS SDKs with KMS, you must get an AWS access key ID and secret access key.
For more information, see:
 - [Sign up for AWS and Create an IAM User](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/signup-create-iam-user.html)
 - [Set Up AWS Credentials and Region for Development](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html)

Create a master key with an alias for simpler configuration.

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
