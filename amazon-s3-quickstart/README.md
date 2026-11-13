# Quarkus demo: Amazon S3 Client

This example showcases how to use the AWS S3 client with Quarkus.

## Run the Demo in Dev Mode

- Run `./mvnw clean quarkus:dev`

Dev Services starts a Floci container and creates the `quarkus.s3.quickstart` bucket automatically, so no AWS account is required.

Go to `http://localhost:8080/s3.html`, it should show a simple App to manage files on your bucket.
You can upload files to the bucket via the form.

Alternatively, go to `http://localhost:8080/async-s3.html` with the simple App communicating with Async resources.

# Run with a local Floci

As a prerequisite, install Docker.
The commands below use the AWS CLI shipped in the Floci `latest-compat` image, so you do not need the AWS CLI on your host.

Start Floci:

```sh
docker run --rm --name floci -p 4566:4566 -d floci/floci:latest-compat
```

S3 listens on `localhost:4566` for REST endpoints.

## Create bucket

Create the bucket used by the application:

```sh
docker exec floci aws s3 mb s3://quarkus.s3.quickstart
```

## Run the demo

You can compile the application and run it with:

```sh
./mvnw install
java \
  -Dquarkus.s3.endpoint-override=http://localhost:4566 \
  -Dquarkus.s3.aws.region=us-east-1 \
  -Dquarkus.s3.aws.credentials.type=static \
  -Dquarkus.s3.aws.credentials.static-provider.access-key-id=test-key \
  -Dquarkus.s3.aws.credentials.static-provider.secret-access-key=test-secret \
  -jar ./target/quarkus-app/quarkus-run.jar
```

Go to `http://localhost:8080/s3.html` or `http://localhost:8080/async-s3.html`, to test the application.

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
./target/amazon-s3-quickstart-1.0.0-SNAPSHOT-runner \
  -Dquarkus.s3.endpoint-override=http://localhost:4566 \
  -Dquarkus.s3.aws.region=us-east-1 \
  -Dquarkus.s3.aws.credentials.type=static \
  -Dquarkus.s3.aws.credentials.static-provider.access-key-id=test-key \
  -Dquarkus.s3.aws.credentials.static-provider.secret-access-key=test-secret
```

# Run the app and Floci in containers

Build a native image in a container by running:

```sh
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
```

Build a Docker image:

```sh
docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-s3-quickstart .
```

Create a network that connects your container with Floci, and start Floci on it:

```sh
docker network create floci
docker run --rm --name floci --network=floci -p 4566:4566 -d floci/floci:latest-compat
```

Create the bucket:

```sh
docker exec floci aws s3 mb s3://quarkus.s3.quickstart
```

Run the Quickstart container connected to that network (note that the application uses the internal Floci URL, `http://floci:4566`):

```sh
docker run -i --rm --network=floci \
  -p 8080:8080 \
  -e QUARKUS_S3_ENDPOINT_OVERRIDE="http://floci:4566" \
  -e QUARKUS_S3_AWS_REGION="us-east-1" \
  -e QUARKUS_S3_AWS_CREDENTIALS_TYPE="static" \
  -e QUARKUS_S3_AWS_CREDENTIALS_STATIC_PROVIDER_ACCESS_KEY_ID="test-key" \
  -e QUARKUS_S3_AWS_CREDENTIALS_STATIC_PROVIDER_SECRET_ACCESS_KEY="test-secret" \
  quarkus/amazon-s3-quickstart
```

Go to `http://localhost:8080/s3.html` or `http://localhost:8080/async-s3.html`, to test the application.

Clean up your environment:

```sh
docker stop floci
docker network rm floci
```

See the [Packaging applications](https://quarkus.io/guides/packaging) guide for more details.

# Using AWS account

As a prerequisite, install the [AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

Before you can use the AWS SDKs with S3, you must get an AWS access key ID and secret access key.
For more information, see:
 - [Sign up for AWS and Create an IAM User](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/signup-create-iam-user.html)
 - [Set Up AWS Credentials and Region for Development](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html)

Create a bucket with a globally unique name:

```sh
aws s3 mb s3://quarkus.s3.quickstart
```

**NOTE: Ensure the bucket name you create is unique across AWS S3 and update the `bucket.name` property accordingly.
See [Amazon S3 Bucket Naming Requirements](https://docs.aws.amazon.com/AmazonS3/latest/dev/BucketRestrictions.html#bucketnamingrules)**

## Run demo

You can run the demo the same way as for a local instance, but you don't need to override the endpoint as you are going to communicate with the AWS service with the default AWS profile.

Run it:

```sh
java -Dbucket.name=quarkus.s3.quickstart -jar ./target/quarkus-app/quarkus-run.jar
```

Or, run it natively:

```sh
./target/amazon-s3-quickstart-1.0.0-SNAPSHOT-runner -Dbucket.name=quarkus.s3.quickstart
```
