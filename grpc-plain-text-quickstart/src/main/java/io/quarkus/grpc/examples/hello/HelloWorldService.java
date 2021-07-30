package io.quarkus.grpc.examples.hello;

import java.util.concurrent.atomic.AtomicInteger;

import examples.Greeter;
import examples.HelloReply;
import examples.HelloRequest;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;

@GrpcService
public class HelloWorldService implements Greeter {

    AtomicInteger counter = new AtomicInteger();

    @Override
    public Uni<HelloReply> sayHello(HelloRequest request) {
        int count = counter.incrementAndGet();
        String name = request.getName();
        return Uni.createFrom().item("Hello " + name)
                .map(res -> HelloReply.newBuilder().setMessage(res).setCount(count).build());
    }
}
