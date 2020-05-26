package org.acme.liquibase;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeMigrationEndpointIT extends MigrationEndpointTest {

    // Execute the same tests but in native mode.
}