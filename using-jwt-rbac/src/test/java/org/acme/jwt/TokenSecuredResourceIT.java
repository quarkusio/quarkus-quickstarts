package org.acme.jwt;

import io.quarkus.test.junit.SubstrateTest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
/**
 * Native tests
 */
@SubstrateTest
public class TokenSecuredResourceIT extends TokenSecuredResourceTest {
   @Test
   @Override
   @Disabled("Doesn't work in the native mode due to a subresource issue")
   public void testLottoWinners() {
   }
}
