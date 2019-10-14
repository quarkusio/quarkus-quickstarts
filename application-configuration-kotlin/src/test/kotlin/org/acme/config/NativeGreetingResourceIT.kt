package org.acme.config

import io.quarkus.test.junit.SubstrateTest

@SubstrateTest
open class NativeGreetingResourceIT : GreetingResourceTest() {}