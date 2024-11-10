package org.acme.context.prices;

import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
@WithTestResource(KafkaResource.class)
public class PriceIT extends PriceTest {

}
