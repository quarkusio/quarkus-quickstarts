package org.acme.ssm;

import java.util.HashMap;
import java.util.Map;

import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class SsmResource implements QuarkusTestResourceLifecycleManager {

    private LocalStackContainer localstack;

    @Override
    public Map<String, String> start() {
        DockerClientFactory.instance().client();
        try {
            localstack = new LocalStackContainer().withServices(Service.SSM);
            localstack.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not start localstack server", e);
        }

        Map<String, String> properties = new HashMap<>();
        properties.put("quarkus.ssm.endpoint-override", endpoint());
        properties.put("quarkus.ssm.aws.region", "us-east-1");
        properties.put("quarkus.ssm.aws.credentials.type", "static");
        properties.put("quarkus.ssm.aws.credentials.static-provider.access-key-id", "accessKey");
        properties.put("quarkus.ssm.aws.credentials.static-provider.secret-access-key", "secretKey");
        properties.put("parameeters.path", "/quarkus/is/awesome/");

        return properties;
    }

    @Override
    public void stop() {
        if (localstack != null) {
            localstack.close();
        }
    }

    private String endpoint() {
        return String.format("http://%s:%s", localstack.getContainerIpAddress(),
                localstack.getMappedPort(Service.SSM.getPort()));
    }
}
