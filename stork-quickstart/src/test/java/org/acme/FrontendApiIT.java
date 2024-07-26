package org.acme;

import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
@WithTestResource(ConsulTestResource.class)
public class FrontendApiIT extends FrontendApiTest {

}
