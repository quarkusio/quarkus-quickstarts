package org.acme.funqy;

import org.junit.jupiter.api.Disabled;

import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
@Disabled("https://github.com/quarkusio/quarkus/issues/7362")
public class FunqyIT extends FunqyTest {

    // Run the same tests
}
