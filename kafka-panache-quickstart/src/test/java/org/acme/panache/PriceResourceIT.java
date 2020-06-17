package org.acme.panache;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
@QuarkusTestResource(KafkaResource.class)
@QuarkusTestResource(DatabaseResource.class)
class PriceResourceIT extends PriceResourceTest {

}