package org.acme.kafka.streams.aggregator.streams;

import io.quarkus.test.junit.QuarkusIntegrationTest;

/**
 * Native tests execute with prod profile, not test. cf https://github.com/quarkusio/quarkus/issues/4371
 * Since we extend the Hotspot tests and share the QuarkusTestResource,
 * this means broker test port MUST be the same as production port.
 */
@QuarkusIntegrationTest
public class AggregatorIT extends AggregatorTest {

}
