package org.acme.getting.started.testing;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class StaticContentTest {

    @TestHTTPResource("index.html")
    URL url;

    @Test
    public void testIndexHtml() throws IOException {
        try (InputStream in = url.openStream()) {
            String contents = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            Assertions.assertTrue(contents.contains("<title>Testing Guide</title>"));
        }
    }

}
