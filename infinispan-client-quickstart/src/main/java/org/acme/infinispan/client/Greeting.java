package org.acme.infinispan.client;

import org.infinispan.protostream.annotations.ProtoField;

public class Greeting {
   @ProtoField(number = 1)
   public String name;

   @ProtoField(number = 2)
   public String message;
}
