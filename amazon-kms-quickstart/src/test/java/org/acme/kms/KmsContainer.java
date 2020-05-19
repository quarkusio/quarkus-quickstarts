package org.acme.kms;

import org.testcontainers.containers.localstack.LocalStackContainer;

/**
 * KMS is not currently supported by LocalStackContainer. This class extends it by bypassing some checks in order to get KMS available.
 */
public class KmsContainer extends LocalStackContainer {

    private static final String HOSTNAME_EXTERNAL_ENV_VAR = "HOSTNAME_EXTERNAL";
    private static final Integer KMS_PORT = 4566;

    public KmsContainer() {
        super("0.11.1");
    }

    @Override
    protected void configure() {
        withEnv("SERVICES", "kms");

        String hostnameExternalReason;
        if (getEnvMap().containsKey(HOSTNAME_EXTERNAL_ENV_VAR)) {
            // do nothing
            hostnameExternalReason = "explicitly as environment variable";
        } else if (getNetwork() != null && getNetworkAliases() != null && getNetworkAliases().size() >= 1) {
            withEnv(HOSTNAME_EXTERNAL_ENV_VAR, getNetworkAliases().get(getNetworkAliases().size() - 1));  // use the last network alias set
            hostnameExternalReason = "to match last network alias on container with non-default network";
        } else {
            withEnv(HOSTNAME_EXTERNAL_ENV_VAR, getContainerIpAddress());
            hostnameExternalReason = "to match host-routable address for container";
        }
        logger().info("{} environment variable set to {} ({})", HOSTNAME_EXTERNAL_ENV_VAR, getEnvMap().get(HOSTNAME_EXTERNAL_ENV_VAR),
            hostnameExternalReason);

        addExposedPort(KMS_PORT);
    }

    public Integer getKmsPort() {
        return KMS_PORT;
    }
}
