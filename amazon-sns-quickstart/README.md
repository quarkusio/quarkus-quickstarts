# Quarkus demo: Amazon SNS Client

This example showcases how to use the AWS SNS client with Quarkus.

## Run the Demo in Dev Mode

- Run `./mvnw clean quarkus:dev`

Dev Services starts a Floci container and creates the `QuarksCollider` topic automatically, so no AWS account is required.

## Subscribe the application to the topic

The application must be subscribed before it can receive notifications.
Subscribe the synchronous and asynchronous shields:

```sh
curl -XPOST -H'Content-type: application/json' http://localhost:8080/sync/shield/subscribe
curl -XPOST -H'Content-type: application/json' http://localhost:8080/async/shield/subscribe
```

NOTE: On Linux, the Floci container started by Dev Services cannot resolve `host.docker.internal`, so it cannot reach the application to confirm the subscription.
On Linux, use a manually started Floci with the `--add-host=host.docker.internal:host-gateway` option (see below) to exercise the shield endpoints.
Publishing with the cannon endpoints works on every platform.

## Publish messages to the topic

Shoot with a couple of quarks using sync endpoint

```sh
curl -XPOST -H'Content-type: application/json' http://localhost:8080/sync/cannon/shoot -d'{"flavor": "Charm", "spin": "1/2"}'
curl -XPOST -H'Content-type: application/json' http://localhost:8080/sync/cannon/shoot -d'{"flavor": "Strange", "spin": "1/2"}'
```

And using async endpoint

```sh
curl -XPOST -H'Content-type: application/json' http://localhost:8080/async/cannon/shoot -d'{"flavor": "Upper", "spin": "1/2"}'
curl -XPOST -H'Content-type: application/json' http://localhost:8080/async/cannon/shoot -d'{"flavor": "Down", "spin": "1/2"}'
```

And observe logs that both endpoints (sync & async) receives published messages

```log
2020-05-13 11:34:02,366 INFO  [org.acm.sns.QuarksShieldSyncResource] (executor-thread-31) Quark[Charm, 1/2] collision with the shield.
2020-05-13 11:34:02,378 INFO  [org.acm.sns.QuarksShieldAsyncResource] (executor-thread-31) Quark[Charm, 1/2] collision with the shield.
2020-05-13 11:34:02,383 INFO  [org.acm.sns.QuarksCannonSyncResource] (executor-thread-32) Fired Quark[Charm, 1/2]
2020-05-13 11:34:18,852 INFO  [org.acm.sns.QuarksShieldSyncResource] (executor-thread-31) Quark[Strange, 1/2] collision with the shield.
2020-05-13 11:34:18,864 INFO  [org.acm.sns.QuarksShieldAsyncResource] (executor-thread-31) Quark[Strange, 1/2] collision with the shield.
2020-05-13 11:34:18,870 INFO  [org.acm.sns.QuarksCannonSyncResource] (executor-thread-32) Fired Quark[Strange, 1/2]
2020-05-13 11:34:26,973 INFO  [org.acm.sns.QuarksShieldSyncResource] (executor-thread-32) Quark[Upper, 1/2] collision with the shield.
2020-05-13 11:34:26,984 INFO  [org.acm.sns.QuarksShieldAsyncResource] (executor-thread-32) Quark[Upper, 1/2] collision with the shield.
2020-05-13 11:34:26,992 INFO  [org.acm.sns.QuarksCannonAsyncResource] (sdk-async-response-11-0) Fired Quark[Upper, 1/2]
2020-05-13 11:34:36,025 INFO  [org.acm.sns.QuarksShieldSyncResource] (executor-thread-32) Quark[Down, 1/2] collision with the shield.
2020-05-13 11:34:36,033 INFO  [org.acm.sns.QuarksShieldAsyncResource] (executor-thread-32) Quark[Down, 1/2] collision with the shield.
2020-05-13 11:34:36,038 INFO  [org.acm.sns.QuarksCannonAsyncResource] (sdk-async-response-11-1) Fired Quark[Down, 1/2]
```

## Unsubscribe endpoint from the topic

Unsubscribe sync endpoint

```sh
curl -XPOST -H'Content-type: application/json' http://localhost:8080/sync/shield/unsubscribe
```

Unsubscribe async endpoint

```sh
curl -XPOST -H'Content-type: application/json' http://localhost:8080/async/shield/unsubscribe
```

And observe the logs to see that endpoint is unsubscribed and will stop receiving new messages from the topic.

```log
2020-05-13 11:39:21,744 INFO  [org.acm.sns.QuarksShieldAsyncResource] (sdk-async-response-4-3) Unsubscribed quarks shield for id = arn:aws:sns:us-east-1:000000000000:QuarksCollider:e1a5c39d-d5fe-498a-b0bb-e2db14dc38b9
2020-05-13 11:39:28,085 INFO  [org.acm.sns.QuarksShieldSyncResource] (executor-thread-1) Unsubscribed quarks shield for id = arn:aws:sns:us-east-1:000000000000:QuarksCollider:90576a46-46d9-4f1a-a8d0-53aa015fffc6
```

# Run with a local Floci

As a prerequisite, install Docker.
The commands below use the AWS CLI shipped in the Floci `latest-compat` image, so you do not need the AWS CLI on your host.

On Linux, add the `--add-host` option so the container can reach the application:

```sh
docker run --rm --name floci -p 4566:4566 --add-host=host.docker.internal:host-gateway -d floci/floci:latest-compat
```

On macOS and Windows, `host.docker.internal` resolves automatically:

```sh
docker run --rm --name floci -p 4566:4566 -d floci/floci:latest-compat
```

SNS listens on `localhost:4566` for REST endpoints.

## Create topic

Create the topic used by the application:

```sh
docker exec floci aws sns create-topic --name=QuarksCollider --query TopicArn --output text
```

## Run the demo

You can compile the application and run it with:

```sh
./mvnw install
java \
  -Dquarkus.sns.endpoint-override=http://localhost:4566 \
  -Dquarkus.sns.aws.region=us-east-1 \
  -Dquarkus.sns.aws.credentials.type=static \
  -Dquarkus.sns.aws.credentials.static-provider.access-key-id=test-key \
  -Dquarkus.sns.aws.credentials.static-provider.secret-access-key=test-secret \
  -jar ./target/quarkus-app/quarkus-run.jar
```

## Subscribe endpoint for topic notifications

Subscribe sync endpoint

```sh
curl -XPOST -H'Content-type: application/json' http://localhost:8080/sync/shield/subscribe
```

Subscribe async endpoint

```sh
curl -XPOST -H'Content-type: application/json' http://localhost:8080/async/shield/subscribe
```

See the Quarkus logs for messages similar to below

```log
2020-05-13 11:31:52,415 INFO  [org.acm.sns.QuarksShieldSyncResource] (executor-thread-2) Subscription confirmed. Ready for quarks collisions.
2020-05-13 11:31:52,434 INFO  [org.acm.sns.QuarksShieldSyncResource] (executor-thread-1) Subscribed Quarks shield <http://host.docker.internal:8080/sync/shield> : arn:aws:sns:us-east-1:000000000000:QuarksCollider:b4121fa3-76e1-4b45-983d-b808561e7700
2020-05-13 11:31:58,847 INFO  [org.acm.sns.QuarksShieldAsyncResource] (sdk-async-response-5-0) Subscription confirmed. Ready for quarks collisions.
2020-05-13 11:31:58,855 INFO  [org.acm.sns.QuarksShieldAsyncResource] (sdk-async-response-5-1) Subscribed Quarks shield with id = arn:aws:sns:us-east-1:000000000000:QuarksCollider:30c06833-a53b-4132-92cf-15017b076a9e
```

You can now replay the `curl` commands from the Dev Mode section above.

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
./target/amazon-sns-quickstart-1.0.0-SNAPSHOT-runner \
  -Dquarkus.sns.endpoint-override=http://localhost:4566 \
  -Dquarkus.sns.aws.region=us-east-1 \
  -Dquarkus.sns.aws.credentials.type=static \
  -Dquarkus.sns.aws.credentials.static-provider.access-key-id=test-key \
  -Dquarkus.sns.aws.credentials.static-provider.secret-access-key=test-secret
```

# Run the app and Floci in containers

Build a native image in a container by running:

```sh
./mvnw install -Dnative -DskipTests -Dquarkus.native.container-build=true
```

Build a Docker image:

```sh
docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-sns-quickstart .
```

Create a network that connects your container with Floci, and start Floci on it:

```sh
docker network create floci
docker run --rm --name floci --network=floci -p 4566:4566 -d floci/floci:latest-compat
```

Create the topic:

```sh
docker exec floci aws sns create-topic --name=QuarksCollider --query TopicArn --output text
```

Run the Quickstart container connected to that network (note that the application uses the internal Floci URL, `http://floci:4566`, and that the shield must reach the application container by its name):

```sh
docker run -i --rm --network=floci \
  -p 8080:8080 \
  --name amazon-sns-quickstart \
  -e QUARKUS_SNS_ENDPOINT_OVERRIDE="http://floci:4566" \
  -e QUARKUS_SNS_AWS_REGION="us-east-1" \
  -e QUARKUS_SNS_AWS_CREDENTIALS_TYPE="static" \
  -e QUARKUS_SNS_AWS_CREDENTIALS_STATIC_PROVIDER_ACCESS_KEY_ID="test-key" \
  -e QUARKUS_SNS_AWS_CREDENTIALS_STATIC_PROVIDER_SECRET_ACCESS_KEY="test-secret" \
  -e QUARKS_SHIELD_BASE_URL="http://amazon-sns-quickstart:8080" \
  quarkus/amazon-sns-quickstart
```

Replay some `curl` commands from above:

```sh
curl -XPOST -H'Content-type: application/json' http://localhost:8080/sync/shield/subscribe
curl -XPOST -H'Content-type: application/json' http://localhost:8080/sync/cannon/shoot -d'{"flavor": "Charm", "spin": "1/2"}'
```

Clean up your environment:

```sh
docker stop floci amazon-sns-quickstart
docker network rm floci
```

See the [Packaging applications](https://quarkus.io/guides/packaging) guide for more details.

# Using AWS account

As a prerequisite, install the [AWS Command Line Interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

Before you can use the AWS SDKs with SNS, you must get an AWS access key ID and secret access key.
For more information, see:
 - [Sign up for AWS and Create an IAM User](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/signup-create-iam-user.html)
 - [Set Up AWS Credentials and Region for Development](https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/setup-credentials.html)

Create a topic using AWS CLI and store the generated ARN in an environment variable as we will need to provide it to our app:

```sh
export TOPIC_ARN=$(aws sns create-topic --name=QuarksCollider --query TopicArn --output text)
```

## Run demo

You can run the demo the same way as for a local instance, but you don't need to override the endpoint as you are going to communicate with the AWS service with the default AWS profile.
You do need to override the topic ARN, and set `quarks.shield.base.url` to an address SNS can reach:

```sh
java -Dtopic.arn=$TOPIC_ARN -Dquarks.shield.base.url=http://<your-host>:8080 -jar ./target/quarkus-app/quarkus-run.jar
```

Or, run it natively:

```sh
./target/amazon-sns-quickstart-1.0.0-SNAPSHOT-runner -Dtopic.arn=$TOPIC_ARN -Dquarks.shield.base.url=http://<your-host>:8080
```
