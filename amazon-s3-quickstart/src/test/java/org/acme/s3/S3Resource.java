package org.acme.s3;

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
import software.amazon.awssdk.services.s3.S3Client;

public class S3Resource implements QuarkusTestResourceLifecycleManager {

    private static final DockerImageName LOCALSTACK_IMAGE_NAME = DockerImageName.parse("localstack/localstack")
            .withTag("0.12.17");
    private final static String BUCKET_NAME = "quarkus.test.bucket";

    private LocalStackContainer container;
    private S3Client client;

    @Override
    public Map<String, String> start() {
        DockerClientFactory.instance().client();
        try {
            container = new LocalStackContainer(LOCALSTACK_IMAGE_NAME).withServices(Service.S3);
            container.start();

            URI endpointOverride = container.getEndpointOverride(EnabledService.named(Service.S3.getName()));

            client = S3Client.builder()
                .endpointOverride(endpointOverride)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("accesskey", "secretKey")))
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.US_EAST_1).build();

            client.createBucket(b -> b.bucket(BUCKET_NAME));

            Map<String, String> properties = new HashMap<>();
            properties.put("quarkus.s3.endpoint-override", endpointOverride.toString());
            properties.put("quarkus.s3.aws.region", "us-east-1");
            properties.put("quarkus.s3.aws.credentials.type", "static");
            properties.put("quarkus.s3.aws.credentials.static-provider.access-key-id", "accessKey");
            properties.put("quarkus.s3.aws.credentials.static-provider.secret-access-key", "secretKey");
            properties.put("bucket.name", BUCKET_NAME);

            return properties;
        } catch (Exception e) {
            throw new RuntimeException("Could not start S3 localstack server", e);
        }
    }

    @Override
    public void stop() {
        if (container != null) {
            container.close();
        }
    }
}
