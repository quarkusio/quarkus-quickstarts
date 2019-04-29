package org.acme.opentracing;

import io.quarkus.test.junit.SubstrateTest;

@SubstrateTest
public class NativeTracedResourceIT extends TracedResourceTest {

    // Execute the same tests but in native mode.
}
