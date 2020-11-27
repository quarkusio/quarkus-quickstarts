package org.acme.quickstart;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeTransactionalResourceIT extends TransactionalResourceTest {

    // Execute the same tests but in native mode.
}
