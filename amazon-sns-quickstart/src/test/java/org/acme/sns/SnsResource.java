package org.acme.sns;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SNS;
import static software.amazon.awssdk.regions.Region.US_EAST_1;

public class SnsResource implements QuarkusTestResourceLifecycleManager {

    public final static String TOPIC_NAME = "Quarkus";
    public final static String LOCALSTACK_IMAGE = "localstack/localstack";
    public final static String LOCALSTACK_VERSION = "0.11.2";
    public final static int SNS_PORT = 4566;

    private LocalStackContainer services;

    @Override
    public Map<String, String> start() {
        DockerClientFactory.instance().client();
        String topicArn;
        try {
            services = new LocalStackContainer(DockerImageName.parse(LOCALSTACK_IMAGE + ":" + LOCALSTACK_VERSION))
                    .withServices(SNS);
            services.start();
            StaticCredentialsProvider staticCredentials = StaticCredentialsProvider
                .create(AwsBasicCredentials.create("accesskey", "secretKey"));

            SnsClient client = SnsClient.builder()
                    .endpointOverride(new URI(endpoint()))
                    .credentialsProvider(staticCredentials)
                    .httpClientBuilder(UrlConnectionHttpClient.builder())
                    .region(US_EAST_1).build();

            topicArn = client.createTopic(t -> t.name(TOPIC_NAME)).topicArn();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not start localstack server", e);
        }

        Map<String, String> properties = new HashMap<>();
        properties.put("quarkus.sns.endpoint-override", endpoint());
        properties.put("quarkus.sns.aws.region", "us-east-1");
        properties.put("quarkus.sns.aws.credentials.type", "static");
        properties.put("quarkus.sns.aws.credentials.static-provider.access-key-id", "accessKey");
        properties.put("quarkus.sns.aws.credentials.static-provider.secret-access-key", "secretKey");
        properties.put("topic.arn", topicArn);

        return properties;
    }

    @Override
    public void stop() {
        if (services != null) {
            services.close();
        }
    }

    private String endpoint() {
        return String.format("http://%s:%s",
                services.getContainerIpAddress(),
                services.getMappedPort(SNS_PORT));
    }
}
