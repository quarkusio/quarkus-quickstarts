package org.acme.funqy;

import io.quarkus.test.junit.NativeImageTest;
import org.junit.jupiter.api.Disabled;

@NativeImageTest
@Disabled("https://github.com/quarkusio/quarkus/issues/7362")
public class FunqyIT extends FunqyTest {

    // Run the same tests
}
