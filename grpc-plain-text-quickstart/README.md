Quarkus gRPC Quickstart
========================

This project illustrates how to expose and consume gRPC services with Quarkus.

## Start the application

The application can be started using: 

```bash
mvn quarkus:dev
```  

## Accessing the application

You can access the REST endpoint with `curl`:
```
curl localhost:8080/hello/blocking/World
```                                     

or
```
curl localhost:8080/hello/mutiny/World
```

The project has gRPC server reflection enabled (see `application.properties`), so you can also access 
the gRPC service directly with [grpcurl](https://github.com/fullstorydev/grpcurl):
```
grpcurl --plaintext -d '{"name": "World!"}' localhost:9000 helloworld.Greeter/SayHello
``` 

## Anatomy
The application has of 2 components:

* `HelloWorldService` - a hello-world gRPC service 
* `HelloWorldEndpoint` - a REST endpoint that uses Quarkus gRPC client to communicate with
the service

The project is configured with `application.properties`.


## Running in native

You can compile the application into a native binary using:

`mvn clean install -Pnative`

and run with:

`./target/grpc-plain-text-quickstart-1.0.0-SNAPSHOT-runner` 
