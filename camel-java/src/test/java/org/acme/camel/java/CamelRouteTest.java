package org.acme.camel.java;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class CamelRouteTest {

    /**
     * Polls {@code target/quarkus.log} as long as {@link CamelRoute#I_AM_ALIVE_MESSAGE} appears there or a timeout of
     * 10 seconds is exceeded.
     */
    @Test
    public void alive() throws IOException {

        final Path logPath = Paths.get(System.getProperty("basedir", "")).resolve("target/quarkus.log");
        final long timeoutMs = 10000;
        final long deadline = timeoutMs  + System.currentTimeMillis();

        try (BufferedReader r = Files.newBufferedReader(logPath, StandardCharsets.UTF_8)) {
            while (System.currentTimeMillis() <= deadline) {
                final String line = r.readLine();
                if (line == null) {
                    /* More lines may appear later */
                    Thread.sleep(100);
                } else if (line.contains(CamelRoute.I_AM_ALIVE_MESSAGE)) {
                    /* test passed */
                    return;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        Assertions.fail(String.format("Log file [%s] should contain [%s] within [%d] ms.", logPath, CamelRoute.I_AM_ALIVE_MESSAGE, timeoutMs));
    }

}
