package org.acme.jms;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.apache.commons.io.FileUtils;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import static org.awaitility.Awaitility.await;

public class ArtemisTestResource implements QuarkusTestResourceLifecycleManager {

    private EmbeddedActiveMQ embedded;

    @Override
    public Map<String, String> start() {
        try {
            FileUtils.deleteDirectory(Paths.get("./target/artemis").toFile());
            embedded = new EmbeddedActiveMQ();
            embedded.start();
            await().until(() -> embedded.getActiveMQServer().isActive()  && embedded.getActiveMQServer().isStarted());
            System.out.println("Artemis server started");
        } catch (Exception e) {
            throw new RuntimeException("Could not start embedded ActiveMQ server", e);
        }
        return Collections.emptyMap();
    }

    @Override
    public void stop() {
        try {
            embedded.stop();
            System.out.println("Artemis server stopped");
        } catch (Exception e) {
            throw new RuntimeException("Could not stop embedded ActiveMQ server", e);
        }
    }
}
