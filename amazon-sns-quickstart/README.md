# Quarkus demo: SNS Client

This example showcases how to use the AWS SNS client with Quarkus. As a prerequisite install Install [AWS Command line interface](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html).

# AWS SNS local instance

 Just run it as follows in order to start SNS locally:
`docker run --rm --name local-sns -p 8009:4575 -e SERVICES=sns -e START_WEB=0 -d localstack/localstack:0.11.1`
SNS listens on `localhost:8009` for REST endpoints.

Create an AWS profile for your local instance using AWS CLI:

```
$ aws configure --profile localstack
AWS Access Key ID [None]: test-key
AWS Secret Access Key [None]: test-secret
Default region name [None]: us-east-1
Default output format [None]:
```

## Create topic
Create a topic using AWS CLI with the localstack profile and store the generated ARN in the environment variable
```
$> TOPIC_ARN=`aws sns create-topic --name=QuarksCollider --profile localstack --endpoint-url=http://localhost:8009`
```

# Run the demo on dev mode
- Run `./mvnw clean package` and then `java -Dtopic.arn=$TOPIC_ARN -jar ./target/quarkus-app/quarkus-run.jar`
- In dev mode `./mvnw clean quarkus:dev -Dtopic.arn=$TOPIC_ARN`

## Subscribe endpoint for topic notifications
Subscribe sync endpoint
`curl -XPOST -H'Content-type: application/json' http://localhost:8080/sync/shield/subscribe`
Subscribe async endpoint
`curl -XPOST -H'Content-type: application/json' http://localhost:8080/async/shield/subscribe`

See the Quarkus logs for messages similar to below
```
2020-05-13 11:31:52,415 INFO  [org.acm.sns.QuarksShieldSyncResource] (executor-thread-2) Subscription confirmed. Ready for quarks collisions.
2020-05-13 11:31:52,434 INFO  [org.acm.sns.QuarksShieldSyncResource] (executor-thread-1) Subscribed Quarks shield <http://host.docker.internal:8080/sync/shield> : arn:aws:sns:us-east-1:000000000000:QuarksCollider:b4121fa3-76e1-4b45-983d-b808561e7700 
2020-05-13 11:31:58,847 INFO  [org.acm.sns.QuarksShieldAsyncResource] (sdk-async-response-5-0) Subscription confirmed. Ready for quarks collisions.
2020-05-13 11:31:58,855 INFO  [org.acm.sns.QuarksShieldAsyncResource] (sdk-async-response-5-1) Subscribed Quarks shield with id = arn:aws:sns:us-east-1:000000000000:QuarksCollider:30c06833-a53b-4132-92cf-15017b076a9e
```

## Publish messages to the topic
Shoot with a couple of quarks using sync endpoint
```
curl -XPOST -H'Content-type: application/json' http://localhost:8080/sync/cannon/shoot -d'{"flavor": "Charm", "spin": "1/2"}'
curl -XPOST -H'Content-type: application/json' http://localhost:8080/sync/cannon/shoot -d'{"flavor": "Strange", "spin": "1/2"}'
```
And using async endpoint
```
curl -XPOST -H'Content-type: application/json' http://localhost:8080/async/cannon/shoot -d'{"flavor": "Upper", "spin": "1/2"}'
curl -XPOST -H'Content-type: application/json' http://localhost:8080/async/cannon/shoot -d'{"flavor": "Down", "spin": "1/2"}'
```

And observe logs that both endpoints (sync & async) receives published messages
```
2020-05-13 11:34:02,366 INFO  [org.acm.sns.QuarksShieldSyncResource] (executor-thread-31) Quark[Charm, 1/2] collision with the shield.
2020-05-13 11:34:02,378 INFO  [org.acm.sns.QuarksShieldAsyncResource] (executor-thread-31) Quark[Charm, 1/2] collision with the shield.
2020-05-13 11:34:02,383 INFO  [org.acm.sns.QuarksCannonSyncResource] (executor-thread-32) Fired Quark[Charm, 1/2}]
2020-05-13 11:34:18,852 INFO  [org.acm.sns.QuarksShieldSyncResource] (executor-thread-31) Quark[Strange, 1/2] collision with the shield.
2020-05-13 11:34:18,864 INFO  [org.acm.sns.QuarksShieldAsyncResource] (executor-thread-31) Quark[Strange, 1/2] collision with the shield.
2020-05-13 11:34:18,870 INFO  [org.acm.sns.QuarksCannonSyncResource] (executor-thread-32) Fired Quark[Strange, 1/2}]
2020-05-13 11:34:26,973 INFO  [org.acm.sns.QuarksShieldSyncResource] (executor-thread-32) Quark[Upper, 1/2] collision with the shield.
2020-05-13 11:34:26,984 INFO  [org.acm.sns.QuarksShieldAsyncResource] (executor-thread-32) Quark[Upper, 1/2] collision with the shield.
2020-05-13 11:34:26,992 INFO  [org.acm.sns.QuarksCannonAsyncResource] (sdk-async-response-11-0) Fired Quark[Upper, 1/2}]
2020-05-13 11:34:36,025 INFO  [org.acm.sns.QuarksShieldSyncResource] (executor-thread-32) Quark[Down, 1/2] collision with the shield.
2020-05-13 11:34:36,033 INFO  [org.acm.sns.QuarksShieldAsyncResource] (executor-thread-32) Quark[Down, 1/2] collision with the shield.
2020-05-13 11:34:36,038 INFO  [org.acm.sns.QuarksCannonAsyncResource] (sdk-async-response-11-1) Fired Quark[Down, 1/2}]

```

## Unsubscribe endpoint from the topic
Unsubscribe sync endpoint
`curl -XPOST -H'Content-type: application/json' http://localhost:8080/sync/shield/unsubscribe`
Unsubscribe async endpoint
`curl -XPOST -H'Content-type: application/json' http://localhost:8080/async/shield/unsubscribe`

And observe the logs to see that endpoint is unsubscribed and will stop receiving new messages from the topic.
```
2020-05-13 11:39:21,744 INFO  [org.acm.sns.QuarksShieldAsyncResource] (sdk-async-response-4-3) Unsubscribed quarks shield for id = arn:aws:sns:us-east-1:000000000000:QuarksCollider:e1a5c39d-d5fe-498a-b0bb-e2db14dc38b9
2020-05-13 11:39:28,085 INFO  [org.acm.sns.QuarksShieldSyncResource] (executor-thread-1) Unsubscribed quarks shield for id = arn:aws:sns:us-east-1:000000000000:QuarksCollider:90576a46-46d9-4f1a-a8d0-53aa015fffc6
```

# Running in native
You can compile the application into a native binary using:
`./mvnw clean install -Pnative`
and run with:
`./target/amazon-sns-quickstart-1.0.0-SNAPSHOT-runner` 

# Running native in container

Build a native image in container by running:
`./mvnw package -Pnative -Dnative-image.docker-build=true`

Build a docker image:
`docker build -f src/main/docker/Dockerfile.native -t quarkus/amazon-sns-quickstart .`

Create a network that connect your container with localstack
`docker network create localstack`

Start localstack and connect to the network
`docker run --rm --network=localstack --name localstack -p 8009:4575 -e SERVICES=sns -e START_WEB=0 -d localstack/localstack:0.11.1`

Stop your localstack container you started at the beginning
`docker stop local-sns`

Run quickstart container connected to `localstack` network (note that we're using internal port of the localstack here)
`docker run -i --rm --network=localstack -p 8080:8080 quarkus/amazon-sns-quickstart -Dquarkus.sns.endpoint-override=http://localstack:4575`

Subscribe an endpoint
`curl -XPOST -H'Content-type: application/json' http://localhost:8080/sync/shield/subscribe`

Publish a message
`curl -XPOST -H'Content-type: application/json' http://localhost:8080/sync/cannon/shoot -d'{"flavor": "Charm", "spin": "1/2"}'`

