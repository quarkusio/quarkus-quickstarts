package org.acme.jwt;

import io.quarkus.test.junit.SubstrateTest;

import org.junit.jupiter.api.Test;
/**
 * Native tests
 */
@SubstrateTest
public class TokenSecuredResourceIT extends TokenSecuredResourceTest {
   @Test
   @Override
   public void testLottoWinners() {
       // This does not currently work in the native mode due to a subresource issue
   }
}
