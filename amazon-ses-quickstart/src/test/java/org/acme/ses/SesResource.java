package org.acme.ses;

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
import software.amazon.awssdk.services.ses.SesClient;

public class SesResource implements QuarkusTestResourceLifecycleManager {

    public final static String FROM_EMAIL = "from-quarkus@example.com";
    public final static String TO_EMAIL = "to-quarkus@example.com";

    private LocalStackContainer services;
    private SesClient client;

    @Override
    public Map<String, String> start() {
        DockerClientFactory.instance().client();
        try {
            services = new LocalStackContainer("0.11.1").withServices(Service.SES);
            services.start();
            StaticCredentialsProvider staticCredentials = StaticCredentialsProvider
                .create(AwsBasicCredentials.create("accesskey", "secretKey"));

            client = SesClient.builder()
                .endpointOverride(new URI(endpoint()))
                .credentialsProvider(staticCredentials)
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .region(Region.US_EAST_1).build();

            client.verifyEmailIdentity(req -> req.emailAddress(FROM_EMAIL));
            client.verifyEmailIdentity(req -> req.emailAddress(TO_EMAIL));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not start localstack server", e);
        }

        Map<String, String> properties = new HashMap<>();
        properties.put("quarkus.ses.endpoint-override", endpoint());
        properties.put("quarkus.ses.aws.region", "us-east-1");
        properties.put("quarkus.ses.aws.credentials.type", "static");
        properties.put("quarkus.ses.aws.credentials.static-provider.access-key-id", "accessKey");
        properties.put("quarkus.ses.aws.credentials.static-provider.secret-access-key", "secretKey");

        return properties;
    }

    @Override
    public void stop() {
        if (services != null) {
            services.close();
        }
    }

    private String endpoint() {
        return String.format("http://%s:%s", services.getContainerIpAddress(), services.getMappedPort(Service.SES.getPort()));
    }
}
