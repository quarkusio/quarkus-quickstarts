package org.acme.dynamodb.enhanced;

import io.smallrye.mutiny.Uni;
import org.acme.dynamodb.Fruit;
import software.amazon.awssdk.enhanced.dynamodb.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@ApplicationScoped
public class FruitAsyncService {
    private DynamoDbAsyncTable<FruitDynamoDbBean> fruitTable;

    @Inject
    FruitAsyncService(DynamoDbEnhancedAsyncClient dynamoEnhancedClient) {
        fruitTable = dynamoEnhancedClient.table("QuarkusFruits",
                TableSchema.fromClass(FruitDynamoDbBean.class));
    }

    public Uni<List<Fruit>> findAll() {

        List<Fruit> listFruit = new LinkedList<>();

        try {
            fruitTable.scan().items().subscribe((a) -> {listFruit.add(convertToFruit(a));}).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return Uni.createFrom().item(listFruit);

    }

    public Uni<List<Fruit>> add(Fruit fruit) {
        return Uni.createFrom().completionStage(() -> fruitTable.putItem(convertToFruitDynamoDbBean(fruit)))
                .onItem().ignore().andSwitchTo(this::findAll);
    }

    public Uni<Fruit> get(String name) {
        Key key = Key.builder().partitionValue(name).build();
        return Uni.createFrom().completionStage(() -> fruitTable.getItem(key))
                .onItem().transform(resp -> convertToFruit(resp));
    }

    private Fruit convertToFruit(FruitDynamoDbBean dynamoDbBean) {
        Fruit fruit = new Fruit();
        fruit.setName(dynamoDbBean.getFruitName());
        fruit.setDescription(dynamoDbBean.getFruitDescription());
        return fruit;
    }

    private FruitDynamoDbBean convertToFruitDynamoDbBean(Fruit fruit) {
        FruitDynamoDbBean fruitDynamoDbBean = new FruitDynamoDbBean();
        fruitDynamoDbBean.setFruitDescription(fruit.getDescription());
        fruitDynamoDbBean.setFruitName(fruit.getName());
        return fruitDynamoDbBean;
    }
}