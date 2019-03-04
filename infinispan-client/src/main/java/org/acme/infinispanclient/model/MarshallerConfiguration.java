package org.acme.infinispanclient.model;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.infinispan.protostream.MessageMarshaller;

@ApplicationScoped
public class MarshallerConfiguration {

   @Produces
   MessageMarshaller characterMarshaller() {
      return new HarryPotterCharacterMarshaller();
   }

   @Produces
   MessageMarshaller spellMarshaller() {
      return new HarryPotterSpellMarshaller();
   }

   @Produces
   MessageMarshaller magicMarshaller() {
      return new HarryPotterMagicMarshaller();
   }
}
