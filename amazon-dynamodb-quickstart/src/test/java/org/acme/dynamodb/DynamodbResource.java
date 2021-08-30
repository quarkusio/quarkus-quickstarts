package org.acme.dynamodb;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.EnabledService;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.utility.DockerImageName;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

public class DynamodbResource implements QuarkusTestResourceLifecycleManager {

    public final static String TABLE_NAME = "QuarkusFruits";

    private static final DockerImageName LOCALSTACK_IMAGE_NAME = DockerImageName.parse("localstack/localstack")
            .withTag("0.12.17");

    private LocalStackContainer container;
    private DynamoDbClient client;

    @Override
    public Map<String, String> start() {
        DockerClientFactory.instance().client();
        try {
            container = new LocalStackContainer(LOCALSTACK_IMAGE_NAME).withServices(Service.DYNAMODB);
            container.start();

            URI endpointOverride = container.getEndpointOverride(EnabledService.named(Service.DYNAMODB.getName()));

            client = DynamoDbClient.builder()
                .endpointOverride(endpointOverride)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("accesskey", "secretKey")))
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.US_EAST_1).build();

            client.createTable(tableRequest ->
                tableRequest.tableName(TABLE_NAME)
                    .keySchema(keySchema -> keySchema.attributeName("fruitName").keyType(KeyType.HASH))
                    .attributeDefinitions(attrDef -> attrDef.attributeName("fruitName").attributeType(ScalarAttributeType.S))
                    .provisionedThroughput(throughput -> throughput.writeCapacityUnits(1L).readCapacityUnits(1L)));

            Map<String, String> properties = new HashMap<>();
            properties.put("quarkus.dynamodb.endpoint-override", endpointOverride.toString());
            properties.put("quarkus.dynamodb.aws.region", "us-east-1");
            properties.put("quarkus.dynamodb.aws.credentials.type", "static");
            properties.put("quarkus.dynamodb.aws.credentials.static-provider.access-key-id", "accessKey");
            properties.put("quarkus.dynamodb.aws.credentials.static-provider.secret-access-key", "secretKey");

            return properties;
        } catch (Exception e) {
            throw new RuntimeException("Could not start Dynamodb localstack server", e);
        }
    }

    @Override
    public void stop() {
        if (container != null) {
            container.close();
        }
    }
}
