package org.acme.infinispanclient.model;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;

public class HarryPotterCharacterMarshaller implements MessageMarshaller<HPCharacter> {

   @Override
   public HPCharacter readFrom(ProtoStreamReader reader) throws IOException {
      int id = reader.readInt("id");
      String name = reader.readString("name");
      String bio = reader.readString("bio");
      int type = reader.readInt("type");
      return new HPCharacter(id, name, bio, type);
   }

   @Override
   public void writeTo(ProtoStreamWriter writer, HPCharacter character) throws IOException {
      writer.writeInt("id", character.getId());
      writer.writeString("name", character.getName());
      writer.writeString("bio", character.getBio());
      writer.writeInt("type", character.getType());
   }

   @Override
   public Class<? extends HPCharacter> getJavaClass() {
      return HPCharacter.class;
   }

   @Override
   public String getTypeName() {
      return "quickstart.HPCharacter";
   }
}
