package org.acme.s3;

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
import software.amazon.awssdk.services.s3.S3Client;

public class S3Resource implements QuarkusTestResourceLifecycleManager {

    private final static String BUCKET_NAME = "quarkus.test.bucket";

    private LocalStackContainer s3;
    private S3Client client;

    @Override
    public Map<String, String> start() {
        DockerClientFactory.instance().client();
        try {
            s3 = new LocalStackContainer().withServices(Service.S3);
            s3.start();

            client = S3Client.builder()
                .endpointOverride(new URI(endpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("accesskey", "secretKey")))
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.US_EAST_1).build();

            client.createBucket(b -> b.bucket(BUCKET_NAME));
        } catch (Exception e) {
            throw new RuntimeException("Could not start S3 localstack server", e);
        }

        Map<String, String> properties = new HashMap<>();
        properties.put("quarkus.s3.endpoint-override", endpoint());
        properties.put("quarkus.s3.aws.region", "us-east-1");
        properties.put("quarkus.s3.aws.credentials.type", "static");
        properties.put("quarkus.s3.aws.credentials.static-provider.access-key-id", "accessKey");
        properties.put("quarkus.s3.aws.credentials.static-provider.secret-access-key", "secretKey");
        properties.put("bucket.name", BUCKET_NAME);

        return properties;
    }

    @Override
    public void stop() {
        if (s3 != null) {
            s3.close();
        }
    }

    private String endpoint() {
        return String.format("http://%s:%s", s3.getContainerIpAddress(), s3.getMappedPort(Service.S3.getPort()));
    }
}
