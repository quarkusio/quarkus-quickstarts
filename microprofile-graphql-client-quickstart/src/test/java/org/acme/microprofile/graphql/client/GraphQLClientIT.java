package org.acme.microprofile.graphql.client;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Disabled;

@QuarkusIntegrationTest
@Disabled("Blocked by https://github.com/quarkusio/quarkus/issues/45334")
public class GraphQLClientIT extends GraphQLClientTest {

}
