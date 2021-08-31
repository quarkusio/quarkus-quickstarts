package org.acme.sqs;

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
import software.amazon.awssdk.services.sqs.SqsClient;

public class SqsResource implements QuarkusTestResourceLifecycleManager {

    private static final DockerImageName LOCALSTACK_IMAGE_NAME = DockerImageName.parse("localstack/localstack")
            .withTag("0.12.17");

    public final static String QUEUE_NAME = "Quarkus";

    private LocalStackContainer container;
    private SqsClient client;

    @Override
    public Map<String, String> start() {
        DockerClientFactory.instance().client();
        String queueUrl;
        try {
            container = new LocalStackContainer(LOCALSTACK_IMAGE_NAME).withServices(Service.SQS);
            container.start();

            URI endpointOverride = container.getEndpointOverride(EnabledService.named(Service.SQS.getName()));

            StaticCredentialsProvider staticCredentials = StaticCredentialsProvider
                .create(AwsBasicCredentials.create("accesskey", "secretKey"));

            client = SqsClient.builder()
                .endpointOverride(endpointOverride)
                .credentialsProvider(staticCredentials)
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.US_EAST_1).build();

            queueUrl = client.createQueue(q -> q.queueName(QUEUE_NAME)).queueUrl();

            Map<String, String> properties = new HashMap<>();
            properties.put("quarkus.sqs.endpoint-override", endpointOverride.toString());
            properties.put("quarkus.sqs.aws.region", "us-east-1");
            properties.put("quarkus.sqs.aws.credentials.type", "static");
            properties.put("quarkus.sqs.aws.credentials.static-provider.access-key-id", "accessKey");
            properties.put("quarkus.sqs.aws.credentials.static-provider.secret-access-key", "secretKey");
            properties.put("queue.url", queueUrl);

            return properties;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not start localstack server", e);
        }
    }

    @Override
    public void stop() {
        if (container != null) {
            container.close();
        }
    }
}
