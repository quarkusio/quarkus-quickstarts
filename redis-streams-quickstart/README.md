# Quarkus Redis Streams Quickstart 

This project illustrates how you can build Redis Stream applications using Quarkus, 
inspired by kafka-streams-quickstart project

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

This quickstart is made up of the following parts:

* Redis Server
* _producer_, a Quarkus application that publishes some test data on `station#` and `temperature-values` redis stream
* _aggregator_, a Quarkus application processing redis-stream, using the Reactive Redis Streams API

The _aggregator_ application is the interesting piece; it

* runs a listener in stream `temperature-values` and groups the values by weather station and emits 
the minimum/maximum temperature value per station to the `temperatures-aggregated` table.
* exposes an HTTP endpoint for getting the current minimum/maximum values
  for a given station using reactive redis commands.

## Building

To build the _producer_ and _aggregator_ applications, run

```bash
mvn clean install
```
## Running
A Docker Compose file is provided for running all the components.
Start all containers by running:

```bash
docker-compose up -d --build
```

### Check the values inside Redis
you can check available keys in redis by using the following command: 

```shell
docker exec -ti redis-streams-quickstart-redis-1 sh -c "redis-cli keys '*'"
```
To retrieve all keys related to published weather station you can use the following command: 

```shell
docker exec -ti redis-streams-quickstart-redis-1 sh -c "redis-cli keys 'station#*'"
```

To retrieve all entries from temperature-aggregates you can use the following command: 

```shell
ocker exec -ti redis-streams-quickstart-redis-1 sh -c "redis-cli hgetall temperature-aggregates" 
```

### Consuming using REST API 
Retrieve the aggregated data from aggregator using the following http command
(your actual host names will differ):

```bash
curl http://localhost:8080/temperatures/{id}
```
where the id you can put the id of the weather stations. 

## Running in native

To run the _producer_ and _aggregator_ applications as native binaries via GraalVM,
first run the Maven builds using the `native` profile:

```bash
mvn clean install -Pnative -Dnative-image.container-runtime=docker -DskipTests=true
```

Then create an environment variable named `QUARKUS_MODE` and with value set to "native":

```bash
export QUARKUS_MODE=native
```

Now start Docker Compose as described above.