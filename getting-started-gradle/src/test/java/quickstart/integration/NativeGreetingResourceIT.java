package quickstart.integration;

import io.quarkus.test.junit.SubstrateTest;
import quickstart.GreetingResourceTest;

@SubstrateTest
public class NativeGreetingResourceIT extends GreetingResourceTest  {
    // Execute the same tests but in native mode.
}
