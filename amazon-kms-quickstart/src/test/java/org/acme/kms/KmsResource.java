package org.acme.kms;

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
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DataKeySpec;

public class KmsResource implements QuarkusTestResourceLifecycleManager {

    private static final DockerImageName LOCALSTACK_IMAGE_NAME = DockerImageName.parse("localstack/localstack")
            .withTag("0.12.17");

    private LocalStackContainer container;
    private KmsClient client;

    @Override
    public Map<String, String> start() {
        DockerClientFactory.instance().client();
        String masterKeyId;
        try {
            container = new LocalStackContainer(LOCALSTACK_IMAGE_NAME).withServices(Service.KMS);
            container.start();

            URI endpointOverride = container.getEndpointOverride(EnabledService.named(Service.KMS.getName()));

            StaticCredentialsProvider staticCredentials = StaticCredentialsProvider
                .create(AwsBasicCredentials.create("accesskey", "secretKey"));

            client = KmsClient.builder()
                .endpointOverride(endpointOverride)
                .credentialsProvider(staticCredentials)
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.US_EAST_1).build();

            masterKeyId = client.createKey().keyMetadata().keyId();
            client.generateDataKey(r -> r.keyId(masterKeyId).keySpec(DataKeySpec.AES_256));


            Map<String, String> properties = new HashMap<>();
            properties.put("quarkus.kms.endpoint-override", endpointOverride.toString());
            properties.put("quarkus.kms.aws.region", "us-east-1");
            properties.put("quarkus.kms.aws.credentials.type", "static");
            properties.put("quarkus.kms.aws.credentials.static-provider.access-key-id", "accessKey");
            properties.put("quarkus.kms.aws.credentials.static-provider.secret-access-key", "secretKey");
            properties.put("key.arn", masterKeyId);

            return properties;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not start KMS server", e);
        }
    }

    @Override
    public void stop() {
        if (container != null) {
            container.close();
        }
    }
}
