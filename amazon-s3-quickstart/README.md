# Quarkus demo: AWS S3 Client

This example showcases how to use the AWS S3 client with Quarkus. As a prerequisite install Install [AWS Command line interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

# S3 local instance

Just run it as follows:
`docker run --rm --name local-s3 -p 8008:4572 -e SERVICES=s3 -e START_WEB=0 -d localstack/localstack`

S3 listens on `localhost:8008` for REST endpoints.

Create an AWS profile for your local instance using AWS CLI:

```
$ aws configure --profile localstack
AWS Access Key ID [None]: test-key
AWS Secret Access Key [None]: test-secret
Default region name [None]: us-east-1
Default output format [None]:
```

## Create bucket

Create a S3 bucket using AWS CLI and the localstack profile.
`aws s3 mb s3://quarkus.s3.quickstart --profile localstack --endpoint-url=http://localhost:8008`

# Run the demo in dev mode

- Run `./mvnw clean package` and then `java -jar ./target/quarkus-app/quarkus-run.jar`
- In dev mode `./mvnw clean quarkus:dev`

Go to `http://localhost:8080/s3.html`, it should show a simple App to manage files on your Bucket. 
You can upload files to the bucket via the form.

Alternatively, go to `http://localhost:8080/async-s3.html` with the simple App communicating with Async resources.

# Running in native

You can compile the application into a native executable using:

`./mvnw clean package -Pnative`

and run with:

`./target/amazon-s3-quickstart-1.0.0-SNAPSHOT-runner` 

# Running native in container (using localstack)

Build a native image in container by running:
`./mvnw package -Pnative -Dnative-image.docker-build=true`

Build a docker image:
`docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-s3-quickstart .`

Create a network that connects your container with localstack
`docker network create localstack`

Stop your localstack container you started at the beginning
`docker stop local-s3`

Start localstack and connect to the network
`docker run --rm --network=localstack --name localstack -p 8008:4572 -e SERVICES=s3 -e START_WEB=0 -d localstack/localstack`

Create a S3 bucket using AWS CLI and the localstack profile.
`aws s3 mb s3://quarkus.s3.quickstart --profile localstack --endpoint-url=http://localhost:8008`

Run quickstart container connected to that network (note that we're using internal port of the S3 localstack)
`docker run -i --rm --network=localstack -p 8080:8080 quarkus/amazon-s3-quickstart -Dquarkus.s3.endpoint-override=http://localstack:4572`

Go to `http://localhost:8080/s3.html` or `http://localhost:8080/async-s3.html`

# Using AWS account

Before you can use the AWS SDKs with S3, you must get an AWS access key ID and secret access key. 
For more information, see:
 - [Sign up for AWS and Create an IAM User](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/signup-create-iam-user.html)
 - [Set Up AWS Credentials and Region for Development](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html)

Create a S3 bucket using AWS CLI using your default AWS profile
`aws s3 mb s3://quarkus.s3.quickstart.11.22.33`

**NOTE: Please assure the bucket name you created is unique across AWS S3. 
See [Amazon S3 Bucket Naming Requirements](https://docs.aws.amazon.com/AmazonS3/latest/dev/BucketRestrictions.html#bucketnamingrules)**

## Run demo

You can run the demo the same way as for a local instance, but you need to change the `application.properties`.

- remove or comment out `quarkus.s3.endpoint-override` - as you are going to communicate with the AWS service now
- remove or comment out `quarkus.s3.aws.region` - region is going to be retrieved via the default providers chain in the following order:
    - `aws.region` system property
    - `region` property from the profile file
- remove or comment out `quarkus.s3.aws.credentials.type` - if not configured the client uses `default` credentials provider chain that looks for credentials in this order:
    - Java System Properties - `aws.accessKeyId` and `aws.secretKey`
    - Environment Variables - `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`
    - Credential profiles file at the default location (`~/.aws/credentials`) shared by all AWS SDKs and the AWS CLI
    
Build the application

`./mvnw clean package`
 
And then run it

`java -jar ./target/quarkus-app/quarkus-run.jar -Dbucket.name=quarkus.s3.12.345.99`

Or, build as native executable

`./mvnw clean package -Pnative` 

And then run it

`./target/amazon-s3-quickstart-1.0.0-SNAPSHOT-runner -Dbucket.name=quarkus.s3.12.345.99` 
