package org.acme.dynamodb;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;

public abstract class AbstractService {

    public static final String PARTITION_KEY = "partitionKey";
    public static final String PARTITION_VALUE = "FRUIT";

    public static final String FRUIT_NAME_COL = "fruitName";
    public static final String FRUIT_DESC_COL = "fruitDescription";

    public String getTableName() {
        return "QuarkusFruits";
    }

    protected QueryRequest queryRequest() {
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":pk", AttributeValue.builder().s(PARTITION_VALUE).build());
        Map<String, String> expressionNames = new HashMap<>();
        expressionNames.put("#name", FRUIT_NAME_COL);
        expressionNames.put("#description", FRUIT_DESC_COL);

        return QueryRequest.builder()
                .tableName(getTableName())
                .keyConditionExpression(PARTITION_KEY + " = :pk")
                .projectionExpression("#name, #description")
                .expressionAttributeValues(expressionValues)
                .expressionAttributeNames(expressionNames)
                .scanIndexForward(true) // ascending (alphabetical) order guaranteed by DynamoDB
                .build();
    }

    protected PutItemRequest putRequest(Fruit fruit) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(PARTITION_KEY, AttributeValue.builder().s(PARTITION_VALUE).build());
        item.put(FRUIT_NAME_COL, AttributeValue.builder().s(fruit.getName()).build());
        item.put(FRUIT_DESC_COL, AttributeValue.builder().s(fruit.getDescription()).build());

        return PutItemRequest.builder()
                .tableName(getTableName())
                .item(item)
                .build();
    }

    protected GetItemRequest getRequest(String name) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(PARTITION_KEY, AttributeValue.builder().s(PARTITION_VALUE).build());
        key.put(FRUIT_NAME_COL, AttributeValue.builder().s(name).build());

        return GetItemRequest.builder()
                .tableName(getTableName())
                .key(key)
                .attributesToGet(FRUIT_NAME_COL, FRUIT_DESC_COL)
                .build();
    }
}
