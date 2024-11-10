package org.acme.optaplanner;

import io.quarkus.test.common.TestResourceScope;
import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;

@WithTestResource(value = H2DatabaseTestResource.class, scope = TestResourceScope.GLOBAL)
public class TestResources {

}
