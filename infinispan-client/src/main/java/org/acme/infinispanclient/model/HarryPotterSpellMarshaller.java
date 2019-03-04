package org.acme.infinispanclient.model;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;

public class HarryPotterSpellMarshaller implements MessageMarshaller<HPSpell> {

   @Override
   public HPSpell readFrom(ProtoStreamReader reader) throws IOException {
      int id = reader.readInt("id");
      String name = reader.readString("name");
      String type = reader.readString("type");
      String desc = reader.readString("desc");
      return new HPSpell(id, name, type, desc);
   }

   @Override
   public void writeTo(ProtoStreamWriter writer, HPSpell character) throws IOException {
      writer.writeInt("id", character.getId());
      writer.writeString("name", character.getName());
      writer.writeString("type", character.getType());
      writer.writeString("desc", character.getDescription());
   }

   @Override
   public Class<? extends HPSpell> getJavaClass() {
      return HPSpell.class;
   }

   @Override
   public String getTypeName() {
      return "quickstart.HPSpell";
   }
}
