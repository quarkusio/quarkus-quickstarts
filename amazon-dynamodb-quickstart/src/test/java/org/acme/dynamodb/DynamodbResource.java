package org.acme.dynamodb;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.jbosslog.JBossLog;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;


import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;
import static software.amazon.awssdk.regions.Region.US_EAST_1;
import static software.amazon.awssdk.services.dynamodb.model.KeyType.HASH;
import static software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType.S;

@JBossLog
public class DynamodbResource implements QuarkusTestResourceLifecycleManager {

    public final static String TABLE_NAME = "QuarkusFruits";
    public final static String LOCALSTACK_IMAGE = "localstack/localstack";

    private LocalStackContainer dynamodb;
    private DynamoDbClient client;

    @Override
    public Map<String, String> start() {
        log.info("Starting DynamoDB Container");
        DockerClientFactory.instance().client();
        try {
            dynamodb = new LocalStackContainer(DockerImageName.parse(LOCALSTACK_IMAGE))
                    .withServices(DYNAMODB);
            dynamodb.start();

            StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider
                    .create(AwsBasicCredentials.create("accessKey", "secretKey"));

            client = DynamoDbClient.builder()
                .endpointOverride(new URI(endpoint()))
                .credentialsProvider(credentialsProvider)
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(US_EAST_1).build();


            KeySchemaElement keySchemaElement = KeySchemaElement.builder()
                    .attributeName("fruitName")
                    .keyType(HASH)
                    .build();

            AttributeDefinition attributeDefinition = AttributeDefinition.builder()
                    .attributeName("fruitName")
                    .attributeType(S)
                    .build();

            ProvisionedThroughput provisionedThroughput = ProvisionedThroughput.builder()
                    .writeCapacityUnits(1L)
                    .readCapacityUnits(1L)
                    .build();

            client.createTable(tableRequest ->
                tableRequest.tableName(TABLE_NAME)
                        .keySchema(keySchemaElement)
                        .attributeDefinitions(attributeDefinition)
                        .provisionedThroughput(provisionedThroughput));
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
        log.info("Stopping DynamoDB Container");
        if (dynamodb != null) {
            dynamodb.close();
        }
    }

    private String endpoint() {
        return String.format("http://%s:%s",
                dynamodb.getContainerIpAddress(),
                dynamodb.getMappedPort(4566));
    }
}
