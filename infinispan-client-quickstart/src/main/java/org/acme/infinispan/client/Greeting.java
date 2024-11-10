package org.acme.infinispan.client;

import org.infinispan.protostream.annotations.Proto;

@Proto
public record Greeting(String name, String message) { }
