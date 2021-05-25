Quarkus Kafka Streams Quickstart
========================

This project illustrates how you can build [Apache Kafka Streams](https://kafka.apache.org/documentation/streams) applications using Quarkus.

## Anatomy

This quickstart is made up of the following parts:

* Apache Kafka and ZooKeeper
* _producer_, a Quarkus application that publishes some test data on two Kafka topics: `weather-stations` and `temperature-values`
* _aggregator_, a Quarkus application processing the two topics, using the Kafka Streams API

The _aggregator_ application is the interesting piece; it

* runs a KStreams pipeline, that joins the two topics (on the weather station id),
groups the values by weather station and emits the minimum/maximum temperature value per station to the `temperatures-aggregated` topic
* exposes an HTTP endpoint for getting the current minimum/maximum values
for a given station using Kafka Streams interactive queries.

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

Now run an instance of the _debezium/tooling_ image which comes with several useful tools such as _kafkacat_ and _httpie_:

```bash
docker run --tty --rm -i --network ks debezium/tooling:1.1
```

In the tooling container, run _kafkacat_ to examine the results of the streaming pipeline:

```bash
kafkacat -b kafka:9092 -C -o beginning -q -t temperatures-aggregated
```

You also can obtain the current aggregated state for a given weather station using _httpie_,
which will invoke an Kafka Streams interactive query for that value:

```bash
http aggregator:8080/weather-stations/data/1
```

## Scaling

Kafka Streams pipelines can be scaled out, i.e. the load can be distributed amongst multiple application instances running the same pipeline.
To try this out, scale the _aggregator_ service to three nodes:

```bash
docker-compose up -d --scale aggregator=3
```

This will spin up two more instances of this service.
The state store that materializes the current state of the streaming pipeline
(which we queried before using the interactive query),
is now distributed amongst the three nodes.
I.e. when invoking the REST API on any of the three instances, it might either be
that the aggregation for the requested weather station id is stored locally on the node receiving the query,
or it could be stored on one of the other two nodes.

As the load balancer of Docker Compose will distribute requests to the _aggregator_ service in a round-robin fashion,
we'll invoke the actual nodes directly.
The application exposes information about all the host names via REST:

```bash
http aggregator:8080/weather-stations/meta-data
```

Retrieve the data from one of the three hosts shown in the response
(your actual host names will differ):

```bash
http cf143d359acc:8080/weather-stations/data/1
```

If that node holds the data for key "1", you'll get a response like this:

```
HTTP/1.1 200 OK
Connection: keep-alive
Content-Length: 74
Content-Type: application/json
Date: Tue, 11 Jun 2019 19:16:31 GMT

{
    "avg": 15.7,
    "count": 11,
    "max": 31.0,
    "min": 3.3,
    "stationId": 1,
    "stationName": "Hamburg"
}
```

Otherwise, the service will send a redirect:

```
HTTP/1.1 303 See Other
Connection: keep-alive
Content-Length: 0
Date: Tue, 11 Jun 2019 19:17:51 GMT
Location: http://72064bb97be9:8080/weather-stations/data/2
```

You can have _httpie_ automatically follow the redirect by passing the `--follow` option:

```bash
http --follow aggregator:8080/weather-stations/data/2
```

## TLS 

In case HTTP is disabled via:

```properties
quarkus.http.insecure-requests=disabled
```

The endpoint URL becomes:

```bash
curl -L --insecure https://aggregator:8443/weather-stations/data/2
```

## Running in native

To run the _producer_ and _aggregator_ applications as native binaries via GraalVM,
first run the Maven builds using the `native` profile:

```bash
mvn clean install -Pnative -Dnative-image.container-runtime=docker
```

Then create an environment variable named `QUARKUS_MODE` and with value set to "native":

```bash
export QUARKUS_MODE=native
```

Now start Docker Compose as described above.

## Running locally

For development purposes it can be handy to run the _producer_ and _aggregator_ applications
directly on your local machine instead of via Docker.
For that purpose, a separate Docker Compose file is provided which just starts Apache Kafka and ZooKeeper, _docker-compose-local.yaml_
configured to be accessible from your host system.
Open this file an editor and change the value of the `KAFKA_ADVERTISED_LISTENERS` variable so it contains your host machine's name or ip address.
Then run:

```bash
docker-compose -f docker-compose-local.yaml up

mvn quarkus:dev -f producer/pom.xml

mvn quarkus:dev -Dquarkus.http.port=8081 -f aggregator/pom.xml
```

Any changes done to the _aggregator_ application will be picked up instantly,
and a reload of the stream processing application will be triggered upon the next Kafka message to be processed.
