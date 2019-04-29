package org.acme.quickstart;

import io.quarkus.test.junit.SubstrateTest;
import org.junit.jupiter.api.Disabled;

@SubstrateTest
@Disabled("java.lang.NullPointerException in native mode")
public class NativeGreetingServiceIT extends GreetingServiceTest {

    // Execute the same tests but in native mode.
}