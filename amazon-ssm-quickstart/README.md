# Quarkus demo: Amazon SSM Client

This example showcases how to use the AWS SSM client with Quarkus. As a prerequisite install [AWS Command line interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

# AWS SSM local instance

Just run it as follows in order to start SSM locally:

`docker run --rm --name local-ssn -p 8014:4583 -e SERVICES=ssm -e START_WEB=0 -d localstack/localstack:0.11.1`

SSM listens on `localhost:8014` for REST endpoints.

Create an AWS profile for your local instance using AWS CLI:

```
$ aws configure --profile localstack
AWS Access Key ID [None]: test-key
AWS Secret Access Key [None]: test-secret
Default region name [None]: us-east-1
Default output format [None]:
```

# Run the demo on dev mode

- Run `./mvnw clean package` and then `java -Dparameters.path=/quarkus/is/awesome/ -jar ./target/quarkus-app/quarkus-run.jar`
- In dev mode `./mvnw clean quarkus:dev -Dparameters.path=/quarkus/is/awesome/`

## Set some parameters
First, add as many paramters as you like using the following patterns for secure and plain parameters:

```
curl -XPUT -H"Content-type: text/plain" "http://localhost:8080/sync/secure?secure=true" -d"stored as cipher text"
curl -XPUT -H"Content-type: text/plain" "http://localhost:8080/sync/plain" -d"stored as plain text"
```

## List all parameters
You can now list the parameters you added:

```
curl http://localhost:8080/sync
```

You should see output like this:

```
{"plain":"stored as plain text","secure":"stored as cipher text"}
```

## Test the async endpoints
Replace `sync` with `async` in the examples above to test the asynchronous endpoints.

# Running in native

You can compile the application into a native binary using:

`./mvnw clean install -Pnative`

and run with:

`./target/amazon-ssm-quickstart-1.0.0-SNAPSHOT-runner -Dparameters.path=/quarkus/is/awesome/` 


# Running native in container

Build a native image in container by running:

`./mvnw clean package -Pnative -Dnative-image.docker-build=true`

Build a docker image:

`docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-ssm-quickstart .`

Create a network that connect your container with localstack:

`docker network create localstack`

Stop your localstack container you started at the beginning:

`docker stop local-ssm`

Start localstack and connect to the network:

`docker run --rm --network=localstack --name localstack -p 8014:4583 -e SERVICES=ssm -e START_WEB=0 -d localstack/localstack:0.11.1`

Run quickstart container connected to that network (note that we're using internal port of the localstack):

`docker run -i --rm --network=localstack -p 8080:8080 -e QUARKUS_SSM_ENDPOINT_OVERRIDE="http://localstack:4583" -e PARAMETERS_PATH="/quarkus/is/awesome/" quarkus/amazon-ssm-quickstart`
