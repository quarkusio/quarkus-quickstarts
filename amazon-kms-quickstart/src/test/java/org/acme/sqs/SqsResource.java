package org.acme.sqs;

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
import software.amazon.awssdk.services.sqs.SqsClient;

public class SqsResource implements QuarkusTestResourceLifecycleManager {

    public final static String QUEUE_NAME = "Quarkus";

    private LocalStackContainer services;
    private SqsClient client;

    @Override
    public Map<String, String> start() {
        DockerClientFactory.instance().client();
        String queueUrl;
        try {
            services = new LocalStackContainer("0.11.1").withServices(Service.SQS);
            services.start();
            StaticCredentialsProvider staticCredentials = StaticCredentialsProvider
                .create(AwsBasicCredentials.create("accesskey", "secretKey"));

            client = SqsClient.builder()
                .endpointOverride(new URI(endpoint()))
                .credentialsProvider(staticCredentials)
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.US_EAST_1).build();

            queueUrl = client.createQueue(q -> q.queueName(QUEUE_NAME)).queueUrl();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not start localstack server", e);
        }

        Map<String, String> properties = new HashMap<>();
        properties.put("quarkus.sqs.endpoint-override", endpoint());
        properties.put("quarkus.sqs.aws.region", "us-east-1");
        properties.put("quarkus.sqs.aws.credentials.type", "static");
        properties.put("quarkus.sqs.aws.credentials.static-provider.access-key-id", "accessKey");
        properties.put("quarkus.sqs.aws.credentials.static-provider.secret-access-key", "secretKey");
        properties.put("queue.url", queueUrl);

        return properties;
    }

    @Override
    public void stop() {
        if (services != null) {
            services.close();
        }
    }

    private String endpoint() {
        return String.format("http://%s:%s", services.getContainerIpAddress(), services.getMappedPort(Service.SQS.getPort()));
    }
}
