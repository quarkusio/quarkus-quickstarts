package org.acme.jwt;

import io.quarkus.test.junit.NativeImageTest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
/**
 * Native tests
 */
@NativeImageTest
public class TokenSecuredResourceIT extends TokenSecuredResourceTest {
   @Test
   @Override
   @Disabled("Doesn't work in the native mode due to a subresource issue")
   public void testLottoWinners() {
   }
}
