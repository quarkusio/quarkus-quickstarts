package org.acme.funqy;

import io.quarkus.amazon.lambda.test.LambdaClient;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class FunqyTest {
    @Test
    public void testSimpleLambdaSuccess() throws Exception {
        Friend friend = new Friend("Bill");
        Greeting out = LambdaClient.invoke(Greeting.class, friend);
        Assertions.assertEquals("Hello Bill", out.getMessage());
    }
}
