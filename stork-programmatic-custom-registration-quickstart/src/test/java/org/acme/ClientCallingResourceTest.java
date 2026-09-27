package org.acme;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.stork.Stork;
import io.smallrye.stork.api.Service;
import io.smallrye.stork.api.ServiceInstance;

@QuarkusTest
public class ClientCallingResourceTest {

    @Test
    public void testDiscoverRegisteredInstances() {
        Stork stork = Stork.getInstance();
        Service service = stork.getService("my-service");

        // Use Stork service discovery to resolve instances registered at startup
        List<ServiceInstance> instances = service.getInstances().await().indefinitely();

        Assertions.assertFalse(instances.isEmpty(),
                "Service discovery should resolve at least one registered instance");

        ServiceInstance instance = instances.get(0);
        Assertions.assertEquals("localhost", instance.getHost());
        Assertions.assertEquals(9000, instance.getPort());
    }

}
