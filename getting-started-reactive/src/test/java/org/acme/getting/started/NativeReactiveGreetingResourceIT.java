package org.acme.getting.started;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeReactiveGreetingResourceIT extends ReactiveGreetingResourceTest {

    // Execute the same tests but in native mode.
}