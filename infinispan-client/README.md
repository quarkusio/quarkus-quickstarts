# Quarkus demo: Infinispan Client

This example showcases how to use Infinispan client with Quarkus. 

# Run infinispan server

- Running with docker `docker run -it -p 11222:11222 jboss/infinispan-server:latest`
- Download the server from `http://www.infinsispan.org` and run `./bin/standalone.sh`

Infinispan server listens in ```localhost:8080``` for REST endpoints.

The quickstart configured another http port in the [microprofile configuration file](/src/main/resources/META-INF/microprofile-config.properties) 
with the following property:
`quarkus.http.port=8081`

# Run the demo

- Run `mvn clean package` and then `java -jar ./target/infinispan-client-runner.jar`
- In dev mode `mvn clean compile quarkus:dev`

Go to `http://localhost:8081/infinispan` and display the message
