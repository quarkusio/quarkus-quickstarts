package org.acme.dynamodb;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@ApplicationScoped
public class FruitAsyncService extends AbstractService {

    @Inject
    DynamoDbAsyncClient dynamoDB;

    public CompletableFuture<List<Fruit>> findAll() {
        return dynamoDB.scan(scanRequest())
                .thenApply(res -> res.items().stream().map(Fruit::from).collect(Collectors.toList()));
    }

    public CompletableFuture<List<Fruit>> add(Fruit fruit) {
        return dynamoDB.putItem(putRequest(fruit)).thenCompose(ret -> findAll());
    }

    public CompletableFuture<Fruit> get(String name) {
        return dynamoDB.getItem(getRequest(name)).thenApply(resp -> Fruit.from(resp.item()));
    }
}