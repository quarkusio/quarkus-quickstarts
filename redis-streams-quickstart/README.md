# Quarkus demo: Redis Streams

In this demo, the usecase used in the [Kafka Streams guide](https://quarkus.io/guides/kafka-streams) has been reimplemented with Redis Streams. 

![architecture](https://quarkus.io/guides/images/kafka-streams-guide-architecture.png)

There are two modules: 
- weather-producer: this application does two things at startup:
    - creates 9 weather stations and saves them as Json in a String datastore in Redis with the following name `station:<stationId>`. 
    - produces random temperatures to the `temperature-values` stream (for these 9 weather stations). The speed in which the temperatures are generated can be adjusted with the `RATE`
environment variable (see also the `.env` file). 
- weather-consumer: reads from the `temperature-values` stream and `station:<stationId>` datastore, joins these two together and generates `TemperatureAggregate` which holds the min, max, count, avg temperatures per station.
The `TemperatureAggregate` is stored in a Hash with the following key: `aggregates`, and with the field `stationId` and the value is a Json representation of `TemperatureAggregate`.    

Under the hood, also the following extensions are used:
 - Reactive Routes, to expose REST endpoints
 - Mutiny as the reactive programming model
 - A Redis database; see below to run one via Docker compose
 - Hibernate Validation, to validate REST parameters
 - ArC, the CDI inspired dependency injection tool with zero overhead
 
## Requirements

To compile and run this demo you will need:

- JDK 1.8+
- GraalVM
- Docker
- Maven 3.6.x

In addition, you will need either a Redis database, or Docker to run one.

### Configuring GraalVM and JDK 1.8+

Make sure that both the `GRAALVM_HOME` and `JAVA_HOME` environment variables have
been set, and that a JDK 1.8+ `java` command is on the path.

See the [Building a Native Executable guide](https://quarkus.io/guides/building-native-image)
for help setting up your environment.

## Building & Running the demo in JVM mode

First build the whole project from the parent folder:

> mvn clean package 

Then run it with Docker Compose:

> docker-compose up -d

This command will start a Redis instance and the two applications.

## Exposed endpoints
 - `GET /temperatures/` => returns the currently calculated temperature aggregates for all the stations.
 - `GET /temperatures/{stationId}` 
 
Note: 
- the stationIds start from 1 to 9.
- the weather-consumer's HTTP port is mapped to ports 8080-8083 (for scaling up to 3 instances). See also the `docker-compose.yml` file.
 
 ## Scaling up
 
 To scale up the weather-producer and/or the weather-consumer, type the following:
 
 > docker-compose -d --scale weather-producer=`<number>` --scale weather-consumer=`<number>`

On my laptop I could easily scale up the producer to 20 instances with only 1 consumer (in JVM mode).

## Connecting to Redis

If you want to access Redis and interact with the data directly, then you can do this as follows:

> docker exec -it `<Redis containerID>` redis-cli

From the redis-cli you can now query directly. For example to list all the keys, type: 

```bash
> keys *
 1) "station:5"
 2) "station:1"
 3) "aggregates"
 4) "station:6"
 5) "station:8"
 6) "temperature-values"
 7) "station:4"
 8) "station:7"
 9) "station:9"
10) "station:2"
11) "station:3"
```
Or to get all the temperature aggregates:

```bash
> hgetall aggregates
 1) "2"
 2) "{\"stationId\":2,\"name\":\"Snowdonia\",\"max\":37.0,\"min\":-21.8,\"avg\":6.8,\"sum\":190.0,\"count\":28}"
 3) "4"
 4) "{\"stationId\":4,\"name\":\"Tokio\",\"max\":37.5,\"min\":-25.5,\"avg\":12.0,\"sum\":361.0,\"count\":30}"
 5) "8"
 6) "{\"stationId\":8,\"name\":\"Oslo\",\"max\":27.3,\"min\":-18.4,\"avg\":5.0,\"sum\":99.0,\"count\":20}"
 7) "9"
 8) "{\"stationId\":9,\"name\":\"Marrakesh\",\"max\":48.1,\"min\":-11.8,\"avg\":21.3,\"sum\":745.0,\"count\":35}"
 9) "7"
10) "{\"stationId\":7,\"name\":\"Porthsmouth\",\"max\":36.9,\"min\":-13.9,\"avg\":12.0,\"sum\":491.0,\"count\":41}"
11) "3"
12) "{\"stationId\":3,\"name\":\"Boston\",\"max\":39.1,\"min\":-20.4,\"avg\":10.9,\"sum\":229.0,\"count\":21}"
13) "6"
14) "{\"stationId\":6,\"name\":\"Svalbard\",\"max\":26.7,\"min\":-51.4,\"avg\":-6.8,\"sum\":-223.0,\"count\":33}"
15) "5"
16) "{\"stationId\":5,\"name\":\"Cusco\",\"max\":38.0,\"min\":-5.8,\"avg\":15.8,\"sum\":457.0,\"count\":29}"
17) "1"
18) "{\"stationId\":1,\"name\":\"Hamburg\",\"max\":53.7,\"min\":-36.0,\"avg\":13.5,\"sum\":323.0,\"count\":24}"
```
Or to get the temperature aggregate for station 1:

```bash
> hget aggregates 1
"{\"stationId\":1,\"name\":\"Hamburg\",\"max\":61.7,\"min\":-36.0,\"avg\":12.0,\"sum\":7759.0,\"count\":645}"
```
