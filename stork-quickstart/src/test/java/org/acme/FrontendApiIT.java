package org.acme;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
@QuarkusTestResource(ConsulTestResource.class)
public class FrontendApiIT extends FrontendApiTest {

}
