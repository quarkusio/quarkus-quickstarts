package org.acme.quarkus.sample;

import io.quarkus.test.junit.NativeImageTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@NativeImageTest
class PriceResourceIT extends PriceResourceTest {

}