# Quarkus demo: Infinispan Client

This example showcases how to use the Infinispan client with Quarkus. 

# Start the Infinispan server

You have two options:

**Option 1:** Running with Docker `docker run -it -p 11222:11222 -e USER="Titus Bramble" -e PASS="Shambles" infinispan/server:latest`

There is a known issue between Docker For Mac and Infinispan Client integration. Explanations can be found in
[this blog post](https://blog.infinispan.org/2018/03/accessing-infinispan-inside-docker-for.html).
You **won't need to do this in production**, but for Docker for Mac users we have to configure the following 
property in the file `src/main/resources/META-INF/resources/hotrod-client.properties`: `quarkus.infinispan-client.client-intelligence=BASIC`

**Option 2:** Download the server (11.x) from https://www.infinispan.org/ and run `./bin/server.sh`.

Infinispan Server listens in `localhost:8080` for REST endpoints.

To avoid conflicts, the quickstart configures another HTTP port in the [configuration file](/src/main/resources/application.properties) 
with the following property:

```
quarkus.http.port=8081
```

If you use an older version of Infinispan or ``Red Hat Data Grid``, you might need to:

- Create a file called `hotrod-client.properties` under `src/main/resources/META-INF/`
- Configure the following property: `infinispan.client.hotrod.protocol_version=2.5`

# Run the demo on dev mode

- Run `mvn clean install` and then `java -jar ./target/quarkus-app/quarkus-run.jar`
- In dev mode `mvn clean quarkus:dev`

Go to http://localhost:8081/infinispan, it should show you a message coming from the Infinispan server.


# Use Docker compose with the native image

Once you built a docker image using the `Dockerfile.native`, you might want to test this
container connecting to a running Infinispan image.

Infinispan needs to be properly started to test this locally, and the containers must be in the same network.

For that, we have provided a docker-compose file. The Infinispan Server container is started first, and the client 
waits for it. This is done this way for local testing purposes. 

Run and wait for start `docker-compose up`

Go to http://localhost:8081/infinispan.
