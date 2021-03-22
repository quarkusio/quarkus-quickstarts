package org.acme.context.prices;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
@QuarkusTestResource(KafkaResource.class)
public class PriceIT extends PriceTest {

}
