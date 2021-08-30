package org.acme.ssm;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.EnabledService;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.utility.DockerImageName;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class SsmResource implements QuarkusTestResourceLifecycleManager {

    private static final DockerImageName LOCALSTACK_IMAGE_NAME = DockerImageName.parse("localstack/localstack")
            .withTag("0.12.17");

    private LocalStackContainer container;

    @Override
    public Map<String, String> start() {
        DockerClientFactory.instance().client();
        try {
            container = new LocalStackContainer(LOCALSTACK_IMAGE_NAME).withServices(Service.SSM);
            container.start();

            URI endpointOverride = container.getEndpointOverride(EnabledService.named(Service.SSM.getName()));

            Map<String, String> properties = new HashMap<>();
            properties.put("quarkus.ssm.endpoint-override", endpointOverride.toString());
            properties.put("quarkus.ssm.aws.region", "us-east-1");
            properties.put("quarkus.ssm.aws.credentials.type", "static");
            properties.put("quarkus.ssm.aws.credentials.static-provider.access-key-id", "accessKey");
            properties.put("quarkus.ssm.aws.credentials.static-provider.secret-access-key", "secretKey");
            properties.put("parameeters.path", "/quarkus/is/awesome/");

            return properties;
        } catch (Exception e) {
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
