package org.acme.infinispanclient.service;

import java.util.Random;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.acme.infinispanclient.model.HPCharacter;
import org.acme.infinispanclient.model.HPMagic;
import org.acme.infinispanclient.model.HPSpell;
import org.infinispan.client.hotrod.RemoteCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.infinispan.client.runtime.Remote;
import io.quarkus.scheduler.api.Scheduled;

@ApplicationScoped
public class HogwartsMagicCreator {
   private static final Logger LOGGER = LoggerFactory.getLogger("HogwartsMagicCreator");

   @Inject
   @Remote(DataLoader.HP_CHARACTERS_NAME)
   private RemoteCache<Integer, HPCharacter> characters;

   @Inject
   @Remote(DataLoader.HP_SPELLS_NAME)
   private RemoteCache<Integer, HPSpell> spells;

   @Inject
   @Remote(DataLoader.HP_MAGIC_NAME)
   private RemoteCache<String, HPMagic> magic;

   private Random randomCharacters = new Random();
   private Random randomSpells = new Random();

   @Scheduled(every = "3s")
   void executeMagic() {
      LOGGER.info("... magic is happening ...");
      Integer characterId = randomCharacters.nextInt(65);
      Integer spellId = randomSpells.nextInt(195);

      HPCharacter character = null;
      HPSpell spell = null;
      try {
         character = characters.get(characterId);
         spell = spells.get(spellId);

      } catch (Exception ex) {
         LOGGER.error(String.format("Character %d or Spell %d not found", character, spell));
      }

      if (character == null || spell == null) return;

      // skip those dirty muggles
      if (!character.canDoMagic()) {
         LOGGER.error(character.getName() + " can't perform magic");
         return;
      }

      // Perform magic
      String id = UUID.randomUUID().toString();
      magic.put(id, new HPMagic(id, character.getName(), spell.getName(), character.isAtHogwarts()));
   }
}