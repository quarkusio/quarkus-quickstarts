package org.acme.getting.started.testing;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeGreetingServiceIT extends GreetingServiceTest {

    // Execute the same tests but in native mode.
}