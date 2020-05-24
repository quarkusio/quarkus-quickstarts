package org.acme.kafka.streams.aggregator.streams;

import io.quarkus.test.junit.NativeImageTest;

/**
 * Native tests execute with prod profile, not test. cf https://github.com/quarkusio/quarkus/issues/4371
 * Since we extend the Hotspot tests and share the QuarkusTestResource,
 * this means broker test port MUST be the same as production port.
 */
@NativeImageTest
public class NativeAggregatorIT extends AggregatorTest {

}
