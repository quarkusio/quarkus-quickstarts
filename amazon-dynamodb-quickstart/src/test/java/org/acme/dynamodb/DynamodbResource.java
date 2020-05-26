package org.acme.dynamodb;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

public class DynamodbResource implements QuarkusTestResourceLifecycleManager {

    public final static String TABLE_NAME = "QuarkusFruits";

    private LocalStackContainer dynamodb;
    private DynamoDbClient client;

    @Override
    public Map<String, String> start() {
        DockerClientFactory.instance().client();
        try {
            dynamodb = new LocalStackContainer().withServices(Service.DYNAMODB);
            dynamodb.start();

            client = DynamoDbClient.builder()
                .endpointOverride(new URI(endpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("accesskey", "secretKey")))
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.US_EAST_1).build();

            client.createTable(tableRequest ->
                tableRequest.tableName(TABLE_NAME)
                    .keySchema(keySchema -> keySchema.attributeName("fruitName").keyType(KeyType.HASH))
                    .attributeDefinitions(attrDef -> attrDef.attributeName("fruitName").attributeType(ScalarAttributeType.S))
                    .provisionedThroughput(throughput -> throughput.writeCapacityUnits(1L).readCapacityUnits(1L)));
        } catch (Exception e) {
            throw new RuntimeException("Could not start Dynamodb localstack server", e);
        }

        Map<String, String> properties = new HashMap<>();
        properties.put("quarkus.dynamodb.endpoint-override", endpoint());
        properties.put("quarkus.dynamodb.aws.region", "us-east-1");
        properties.put("quarkus.dynamodb.aws.credentials.type", "static");
        properties.put("quarkus.dynamodb.aws.credentials.static-provider.access-key-id", "accessKey");
        properties.put("quarkus.dynamodb.aws.credentials.static-provider.secret-access-key", "secretKey");

        return properties;
    }

    @Override
    public void stop() {
        if (dynamodb != null) {
            dynamodb.close();
        }
    }

    private String endpoint() {
        return String.format("http://%s:%s", dynamodb.getContainerIpAddress(), dynamodb.getMappedPort(Service.DYNAMODB.getPort()));
    }
}
