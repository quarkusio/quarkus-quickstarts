package org.acme.security.jwt;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class TokenSecuredResourceIT extends TokenSecuredResourceTest {

    // Execute the same tests but in native mode.
}
