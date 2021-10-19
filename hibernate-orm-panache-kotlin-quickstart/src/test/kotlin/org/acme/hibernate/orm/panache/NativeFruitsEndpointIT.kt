package org.acme.hibernate.orm.panache

import io.quarkus.test.junit.NativeImageTest

@NativeImageTest
class NativeFruitsEndpointIT : FruitsEndpointTest() { // Runs the same tests as the parent class
}