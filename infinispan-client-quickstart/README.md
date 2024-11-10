# Quarkus demo: Infinispan Client

# Run the demo on dev mode with Dev Services

- Run `mvn clean install` and then `java -jar ./target/quarkus-app/quarkus-run.jar`
- In dev mode `mvn clean quarkus:dev`
- Interact with the rest api (change the port if you configured:

```bash
# Post a greeting -> Should display Greeting added!
curl -X POST http://localhost:8080/greeting/quarkus -H "Content-Type: application/json" -d '{"name" : "Infinispan Client", "message":"Hello World, Infinispan is up!"}' 

# Get the greeting -> Should display {"name":"Infinispan Client","message":"Hello World, Infinispan is up!"} 
curl  http://localhost:8080/greeting/quarkus
```

# Run the demo on Prod mode with Docker compose and the native image

Once you built a docker image using the `Dockerfile.native`, you might want to test this
container connecting to a running Infinispan image.

Infinispan needs to be properly started to test this locally, and the containers must be in the same network.

For that, we have provided a docker-compose file. The Infinispan Server container is started first, and the client 
waits for it. This is done this way for local testing purposes. 

Run and wait for start `docker-compose up`
