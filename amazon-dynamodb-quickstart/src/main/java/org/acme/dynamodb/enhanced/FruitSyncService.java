package org.acme.dynamodb.enhanced;

import org.acme.dynamodb.Fruit;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FruitSyncService {
    private DynamoDbTable<FruitDynamoDbBean> fruitTable;

    @Inject
    FruitSyncService(DynamoDbEnhancedClient dynamoEnhancedClient) {
        fruitTable = dynamoEnhancedClient.table("QuarkusFruits",
                TableSchema.fromClass(FruitDynamoDbBean.class));
    }

    public List<Fruit> findAll() {
        return fruitTable.scan().items().stream().collect(Collectors.toList()).stream().map(this::convertToFruit).collect(Collectors.toList());
    }

    public List<Fruit> add(Fruit fruit) {
        fruitTable.putItem(convertToFruitDynamoDbBean(fruit));
        return findAll();
    }

    public Fruit get(String name) {
        Key partitionKey = Key.builder().partitionValue(name).build();
        return convertToFruit(fruitTable.getItem(partitionKey));
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