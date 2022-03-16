package io.quarkus.grpc.examples.hello;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import examples.Greeter;
import examples.GreeterGrpc;
import examples.HelloReply;
import examples.HelloRequest;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;

@Path("/hello")
public class HelloWorldTlsEndpoint {

    @GrpcClient("hello")
    GreeterGrpc.GreeterBlockingStub blockingHelloService;

    @GrpcClient("hello")
    Greeter helloService;

    @GET
    @Path("/blocking/{name}")
    public String helloBlocking(String name) {
        return blockingHelloService.sayHello(HelloRequest.newBuilder().setName(name).build()).getMessage();
    }

    @GET
    @Path("/mutiny/{name}")
    public Uni<String> helloMutiny(String name) {
        return helloService.sayHello(HelloRequest.newBuilder().setName(name).build())
                .onItem().transform(HelloReply::getMessage);
    }
}
