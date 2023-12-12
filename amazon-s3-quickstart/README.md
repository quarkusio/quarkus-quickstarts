# Quarkus demo: Amazon S3 Client

This example showcases how to use the AWS S3 client with Quarkus.

## Run the Demo in Dev Mode

- Run `./mvnw clean quarkus:dev`

Go to `http://localhost:8080/s3.html`, it should show a simple App to manage files on your Bucket. 
You can upload files to the bucket via the form.

Alternatively, go to `http://localhost:8080/async-s3.html` with the simple App communicating with Async resources.

# Using LocalStack

As a prerequisite, install the [AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

Start LocalStack:

 ```sh
 docker run \
  --rm \
  --name local-s3 \
  -p 4566:4566 \
  locals

S3 listens on `localhost:4566` for REST endpoints.

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

## Create bucket

Create a S3 bucket using AWS CLI and the localstack profile.

```sh
aws s3 mb s3://quarkus.s3.quickstart --profile localstack --endpoint-url=http://localhost:4566`
```

## Run the demo

You can compile the application and run it with:

```sh
./mvnw install
AWS_PROFILE=localstack java -Dquarkus.s3.endpoint-override=http://localhost:4566 -jar ./target/quarkus-app/quarkus-run.jar
```

Go to `http://localhost:8080/s3.html` or `http://localhost:8080/async-s3.html`, to test the application.

## Running in native

You can compile the application into a native executable using:

```sh
./mvnw install -Dnative
```

And run it with:

```sh
AWS_PROFILE=localstack ./target/amazon-s3-quickstart-1.0.0-SNAPSHOT-runner -Dquarkus.s3.endpoint-override=http://localhost:4566
```

# Running native in container

Build a native image in a container by running:

```sh
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
```

Build a Docker image:

```sh
docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-s3-quickstart .
```

Create a network that connects your container with LocalStack:

```sh
docker network create localstack
```

Stop your LocalStack container you started at the beginning:

```sh
docker stop local-s3
```

Start LocalStack and connect to the network:

```sh
docker run \
  --rm \
  --name local-s3 \
  --network=localstack \
  -p 4566:4566 \
  localstack/localstack
```

Create a S3 bucket using AWS CLI and the localstack profile.

```sh
aws s3 mb s3://quarkus.s3.quickstart --profile localstack --endpoint-url=http://localhost:4566`
```

Run the Quickstart container connected to that network (note that we're using the internal port of the LocalStack container):

```sh
docker run -i --rm --network=localstack \
  -p 8080:8080 \
  -e QUARKUS_S3_ENDPOINT_OVERRIDE="http://local-s3:4566" \
  -e QUARKUS_S3_AWS_REGION="us-east-1" \
  -e QUARKUS_S3_AWS_CREDENTIALS_TYPE="static" \
  -e QUARKUS_S3_AWS_CREDENTIALS_STATIC_PROVIDER_ACCESS_KEY_ID="test-key" \
  -e QUARKUS_S3_AWS_CREDENTIALS_STATIC_PROVIDER_SECRET_ACCESS_KEY="test-secret" \
  -e QUARKUS_S3_PATH_STYLE_ACCESS="true" \
  quarkus/amazon-s3-quickstart
```

Go to `http://localhost:8080/s3.html` or `http://localhost:8080/async-s3.html`, to test the application.

Clean up your environment:

```sh
docker stop local-s3
docker network rm localstack
```

# Using AWS account

Before you can use the AWS SDKs with S3, you must get an AWS access key ID and secret access key.
For more information, see:
 - [Sign up for AWS and Create an IAM User](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/signup-create-iam-user.html)
 - [Set Up AWS Credentials and Region for Development](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html)


Create a S3 bucket using AWS CLI and the localstack profile.

```sh
aws s3 mb s3://quarkus.s3.12.345.99`
```

**NOTE: Please assure the bucket name you created is unique across AWS S3 and update . 
See [Amazon S3 Bucket Naming Requirements](https://docs.aws.amazon.com/AmazonS3/latest/dev/BucketRestrictions.html#bucketnamingrules)**

## Run demo

You can run the demo the same way as for a local instance, but you don't need to override the endpoint as you are going to communicate with the AWS service with the default AWS profile.

Run it:

```sh
java -Dbucket.name=quarkus.s3.12.345.99 -jar ./target/quarkus-app/quarkus-run.jar
```

Or, run it natively:

```sh
./target/amazon-s3-quickstart-1.0.0-SNAPSHOT-runner -Dbucket.name=quarkus.s3.12.345.99
```
