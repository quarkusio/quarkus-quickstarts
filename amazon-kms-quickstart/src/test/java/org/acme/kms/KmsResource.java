package org.acme.kms;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.testcontainers.DockerClientFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DataKeySpec;

public class KmsResource implements QuarkusTestResourceLifecycleManager {

    private KmsContainer services;
    private KmsClient client;

    @Override
    public Map<String, String> start() {
        DockerClientFactory.instance().client();
        String masterKeyId;
        try {
            services = new KmsContainer();
            services.start();
            StaticCredentialsProvider staticCredentials = StaticCredentialsProvider
                .create(AwsBasicCredentials.create("accesskey", "secretKey"));

            client = KmsClient.builder()
                .endpointOverride(services.getEndpointOverride())
                .credentialsProvider(staticCredentials)
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.US_EAST_1).build();

            masterKeyId = client.createKey().keyMetadata().keyId();
            client.generateDataKey(r -> r.keyId(masterKeyId).keySpec(DataKeySpec.AES_256));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not start localstack server", e);
        }

        Map<String, String> properties = new HashMap<>();
        properties.put("quarkus.kms.endpoint-override", services.getEndpointOverride().toString());
        properties.put("quarkus.kms.aws.region", "us-east-1");
        properties.put("quarkus.kms.aws.credentials.type", "static");
        properties.put("quarkus.kms.aws.credentials.static-provider.access-key-id", "accessKey");
        properties.put("quarkus.kms.aws.credentials.static-provider.secret-access-key", "secretKey");
        properties.put("key.arn", masterKeyId);

        return properties;
    }

    @Override
    public void stop() {
        if (services != null) {
            services.close();
        }
    }
}
