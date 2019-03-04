package org.acme.infinispanclient.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.acme.infinispanclient.model.HPCharacter;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.QueryBuilder;
import org.infinispan.query.dsl.QueryFactory;

import io.quarkus.infinispan.client.runtime.Remote;

@ApplicationScoped
public class CharacterSearch {

   @Inject
   @Remote(DataLoader.HP_CHARACTERS_NAME)
   private RemoteCache<Integer, HPCharacter> characters;

   public HPCharacter getById(Integer id) {
      return characters.get(id);
   }

   /**
    * Performs a simple full-text query on name and bio
    * @param term
    * @return character names
    */
   public List<String> search(String term) {
      QueryFactory queryFactory = Search.getQueryFactory(characters);

      QueryBuilder qb = queryFactory.from(HPCharacter.class)
            .having("name").like("%" + term + "%")
            .or()
            .having("bio").like("%" + term + "%");

      List<HPCharacter> characters = qb.build().list();
      return characters.stream().map(HPCharacter::getName).collect(Collectors.toList());
   }

}
