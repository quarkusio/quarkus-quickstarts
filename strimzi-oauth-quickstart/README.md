Quarkus Strimzi OAuth Quickstart
=================================

This project illustrates how you can build an Apache Kafka application authenticating against [Strimzi](https://strimzi.io/) using OAuth. 

## Anatomy

This quickstart is made up of the following parts:

* [https://www.keycloak.org/](Keycloak) 
* Apache ZooKeeper
* _strimzi_ building a containerized version of Kafka configured to use OAuth
* _application_, a Quarkus application that publishes and consumes some test data on the _a_people_ Kafka topic.

The interesting part is not the application, but how it is configured to authenticate with the Kafka broker using OAuth.
The application configuration is in the [application.properties](application/src/main/resources/application.properties).
The interesting part is the following:

```properties
mp.messaging.connector.smallrye-kafka.security.protocol=SASL_PLAINTEXT
mp.messaging.connector.smallrye-kafka.sasl.mechanism=OAUTHBEARER
mp.messaging.connector.smallrye-kafka.sasl.jaas.config=org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required \
  oauth.client.id="team-a-client" \
  oauth.client.secret="team-a-client-secret" \
  oauth.token.endpoint.uri="http://keycloak:8080/auth/realms/kafka-authz/protocol/openid-connect/token" ;
mp.messaging.connector.smallrye-kafka.sasl.login.callback.handler.class=io.strimzi.kafka.oauth.client.JaasClientOauthLoginCallbackHandler
```

If configures the OAuth authentication.


## Building the application

Navigate to the `application` directory and run:

```bash
mvn clean package
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/strimzi-oauth-quickstart-jvm .
```

This command built a containerized version of the application (`quarkus/strimzi-oauth-quickstart-jvm`), running in JVM mode.

Also, build the native executable variant (`quarkus/strimzi-oauth-quickstart`) using:

```bash
mvn package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t quarkus/strimzi-oauth-quickstart .
```

## Running with OAuth over Plain

A Docker Compose file is provided for running all the components required to run Kafka (via Strimzi) using OAuth authentication.
Start all containers by running:

```bash
docker-compose -f docker-compose-oauth-over-plain.yaml up --build -d
```

Be warned, starting the infrastructure may take some time.
Now, start the application:

**In JVM mode (port 8080)**

```bash
docker run -i --rm -p 8080:8080 --network strimzi quarkus/strimzi-oauth-quickstart-jvm
```

**In native mode (port 8081)**

```bash
docker run -i --rm -p 8081:8080 --network strimzi quarkus/strimzi-oauth-quickstart
```

**IMPORTANT:** For both command, we specify the _network_ to be in the same network as Kafka, Zookeeper and Keycloak.

To check that the application works correctly just run:

```bash 
# JVM mode:
> curl localhost:8080/kafka/people
[{"name":"bob"},{"name":"alice"},{"name":"tom"},{"name":"jerry"},{"name":"anna"},{"name":"ken"},{"name":"bob"},{"name":"alice"},{"name":"tom"},{"name":"jerry"},{"name":"anna"},{"name":"ken"}]

# Native mode
> curl localhost:8081/kafka/people
[{"name":"bob"},{"name":"alice"},{"name":"tom"},{"name":"jerry"},{"name":"anna"},{"name":"ken"},{"name":"bob"},{"name":"alice"},{"name":"tom"},{"name":"jerry"},{"name":"anna"},{"name":"ken"}]
```

**TIP:** Stop the running infrastructure using:
```shell script
docker-compose -f docker-compose-oauth-over-plain.yaml down; 
docker-compose -f docker-compose-oauth-over-plain.yaml rm
```


## Authorization support

You can also enable authorization.
A Docker Compose file is provided for running all the components required to run Kafka (via Strimzi) using OAuth authentication and authorization.
Start all containers by running:

```bash
docker-compose -f docker-compose-oauth-authz.yaml up --build -d
```
Be warned, starting the infrastructure may take some time.
Now start the application:

**In JVM mode (port 8080)**

```bash
docker run -i --rm -p 8080:8080 --network strimzi quarkus/strimzi-oauth-quickstart-jvm
```

**In native mode (port 8081)**

```bash
docker run -i --rm -p 8081:8080 --network strimzi quarkus/strimzi-oauth-quickstart
```

**IMPORTANT:** For both command, we specify the _network_ to be in the same network as Kafka, Zookeeper and Keycloak.

To check that the application works correctly just run:

```bash 
# JVM mode:
> curl localhost:8080/kafka/people
[{"name":"bob"},{"name":"alice"},{"name":"tom"},{"name":"jerry"},{"name":"anna"},{"name":"ken"},{"name":"bob"},{"name":"alice"},{"name":"tom"},{"name":"jerry"},{"name":"anna"},{"name":"ken"}]

# Native mode
> curl localhost:8081/kafka/people
[{"name":"bob"},{"name":"alice"},{"name":"tom"},{"name":"jerry"},{"name":"anna"},{"name":"ken"},{"name":"bob"},{"name":"alice"},{"name":"tom"},{"name":"jerry"},{"name":"anna"},{"name":"ken"}]
```

**TIP:** Stop the running infrastructure using:
```shell script
docker-compose -f docker-compose-oauth-authz.yaml down 
docker-compose -f docker-compose-oauth-authz.yaml rm
```