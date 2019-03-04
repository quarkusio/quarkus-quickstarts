package org.acme.infinispanclient.model;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;

public class HarryPotterMagicMarshaller implements MessageMarshaller<HPMagic> {

   @Override
   public HPMagic readFrom(ProtoStreamReader reader) throws IOException {
      String id = reader.readString("id");
      String author = reader.readString("author");
      String spell = reader.readString("spell");
      boolean h = reader.readBoolean("hogwarts");
      return new HPMagic(id, author, spell, h);
   }

   @Override
   public void writeTo(ProtoStreamWriter writer, HPMagic magic) throws IOException {
      writer.writeString("id", magic.getId());
      writer.writeString("author", magic.getAuthor());
      writer.writeString("spell", magic.getSpell());
      writer.writeBoolean("hogwarts", magic.isHogwarts());
   }

   @Override
   public Class<? extends HPMagic> getJavaClass() {
      return HPMagic.class;
   }

   @Override
   public String getTypeName() {
      return "quickstart.HPMagic";
   }
}
