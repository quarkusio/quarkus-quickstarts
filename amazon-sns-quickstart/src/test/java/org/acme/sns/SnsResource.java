package org.acme.sns;

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
import software.amazon.awssdk.services.sns.SnsClient;

public class SnsResource implements QuarkusTestResourceLifecycleManager {

    private static final DockerImageName LOCALSTACK_IMAGE_NAME = DockerImageName.parse("localstack/localstack")
            .withTag("0.12.17");

    public final static String TOPIC_NAME = "Quarkus";

    private LocalStackContainer container;
    private SnsClient client;

    @Override
    public Map<String, String> start() {
        DockerClientFactory.instance().client();
        String topicArn;
        try {
            container = new LocalStackContainer(LOCALSTACK_IMAGE_NAME).withServices(Service.SNS);
            container.start();

            URI endpointOverride = container.getEndpointOverride(EnabledService.named(Service.SNS.getName()));

            StaticCredentialsProvider staticCredentials = StaticCredentialsProvider
                .create(AwsBasicCredentials.create("accesskey", "secretKey"));

            client = SnsClient.builder()
                .endpointOverride(endpointOverride)
                .credentialsProvider(staticCredentials)
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.US_EAST_1).build();

            topicArn = client.createTopic(t -> t.name(TOPIC_NAME)).topicArn();

            Map<String, String> properties = new HashMap<>();
            properties.put("quarkus.sns.endpoint-override", endpointOverride.toString());
            properties.put("quarkus.sns.aws.region", "us-east-1");
            properties.put("quarkus.sns.aws.credentials.type", "static");
            properties.put("quarkus.sns.aws.credentials.static-provider.access-key-id", "accessKey");
            properties.put("quarkus.sns.aws.credentials.static-provider.secret-access-key", "secretKey");
            properties.put("topic.arn", topicArn);

            return properties;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not start ocalstack server", e);
        }
    }

    @Override
    public void stop() {
        if (container != null) {
            container.close();
        }
    }
}
