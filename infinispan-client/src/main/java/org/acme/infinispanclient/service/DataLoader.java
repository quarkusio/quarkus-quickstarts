package org.acme.infinispanclient.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.acme.infinispanclient.model.HPCharacter;
import org.acme.infinispanclient.model.HPMagic;
import org.acme.infinispanclient.model.HPSpell;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class DataLoader {
   public static final String HP_CHARACTERS_NAME = "characters";
   public static final String HP_SPELLS_NAME = "spells";
   public static final String HP_MAGIC_NAME = "magic";

   private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class.getName());

   @Inject
   private RemoteCacheManager cacheManager;

   void onStart(@Observes StartupEvent ev) {
      LOGGER.info("On start - clean and load");
      // Get or create caches
      RemoteCache<Integer, HPCharacter> characters = cacheManager.administration().getOrCreateCache(HP_CHARACTERS_NAME, "default");
      RemoteCache<Integer, HPSpell> spells = cacheManager.administration().getOrCreateCache(HP_SPELLS_NAME, "default");
      RemoteCache<String, HPMagic> magic = cacheManager.administration().getOrCreateCache(HP_MAGIC_NAME, "default");

      // Cleanup data
      cleanupCaches(characters, spells, magic);

      // Load Ref data
      loadData(characters, spells);
   }

   private void loadData(RemoteCache<Integer, HPCharacter> characters, RemoteCache<Integer, HPSpell> spells) {
      try {
         loadCharacters(characters);
         LOGGER.info("Characters loaded. Size: " + characters.size());
         loadSpells(spells);
         LOGGER.info("Spells loaded. Size: " + spells.size());
      } catch (Exception e) {
         LOGGER.error("Unable to load data on startup", e);
      }
   }

   private void cleanupCaches(RemoteCache<Integer, HPCharacter> characters, RemoteCache<Integer, HPSpell> spells, RemoteCache<String, HPMagic> magic) {
      try {
         CompletableFuture.allOf(characters.clearAsync(), spells.clearAsync(), magic.clearAsync()).get(10, TimeUnit.SECONDS);
      } catch (Exception e) {
         LOGGER.error("Something went wrong clearing caches", e);
      }
   }

   /**
    * Load characters into the cache
    *
    * @param cache, characters cache
    * @throws Exception
    */
   private void loadCharacters(RemoteCache<Integer, HPCharacter> cache) throws Exception {
      try (BufferedReader br = new BufferedReader(new FileReader("META-INF/resources/hp_characters.csv"))) {
         String line;
         int id = 0;
         while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            int type = Integer.parseInt(values[0].trim());
            HPCharacter character = new HPCharacter(id, values[1].trim(), values[2].trim(), type);
            cache.put(id, character);
            id++;
         }
      }
   }

   /**
    * Load spells into the cache
    *
    * @param cache, spells cache
    * @throws Exception
    */
   private void loadSpells(RemoteCache<Integer, HPSpell> cache) throws Exception {
      try (BufferedReader br = new BufferedReader(new FileReader("META-INF/resources/hp_spells.csv"))) {
         String line;
         int id = 0;
         while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            HPSpell spell = new HPSpell(id, values[0].trim(), values[1].trim(), values[2].trim());
            cache.put(id, spell);
            id++;
         }
      }
   }
}