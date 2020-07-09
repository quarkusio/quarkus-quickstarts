package org.acme.infinispanclient;

import io.quarkus.test.junit.NativeImageTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@NativeImageTest
class InfinispanGreetingResourceIT extends InfinispanGreetingResourceTest {
}