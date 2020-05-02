# Quarkus demo: Infinispan Embedded

This example showcases how to use Infinispan embedded with Quarkus. 

By default, a clustered version will be started. You can configure the cache mode in the application.properties.

The default configuration uses the following values:

* svc.cache.mode                              =DIST_ASYNC
* svc.cache.entry.lifespan.hours              =16

# Run the demo on dev mode

- Run `mvn clean package` and then `java -jar ./target/quarkus-infinispan-embedded-quickstart-1.0.0-SNAPSHOT-runner.jar`
- In dev mode `mvn clean quarkus:dev`

User an HTTP POST with the URL `http://localhost:8080/tokens` to create a new token.
The payload should look like this:

`{
     "taxCode": "CBTPGA90B13B345H"
 }`
 
 The response will look like this:
 `{
      "token": "b18e9c18-93ba-48b9-a4cd-de39dec32e4b",
      "uri": "/tokens/b18e9c18-93ba-48b9-a4cd-de39dec32e4b"
  }`

Use an HTTP GET with `http://localhost:8080/tokens/{YOUR_TOKEN}`, it should show you a message coming from the Infinispan server.

# Run the demo on Openshift

In order to run the demo on Openshift you can activate the profile ocp-ext executing the following command:
`./mvnw package -Pocp-ext -Dquarkus.kubernetes.deploy=true`

