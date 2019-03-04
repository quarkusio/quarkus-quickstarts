package org.acme.infinispanclient;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.acme.infinispanclient.model.HPMagic;
import org.acme.infinispanclient.service.DataLoader;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.api.continuous.ContinuousQuery;
import org.infinispan.query.api.continuous.ContinuousQueryListener;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.infinispan.client.runtime.Remote;

@ServerEndpoint("/harry-potter/hogwarts")
@ApplicationScoped
public class HogwartsMagicSocket {

   private static final Logger LOG = LoggerFactory.getLogger("HogwartsMagicSocket");

   @Inject
   @Remote(DataLoader.HP_MAGIC_NAME)
   private RemoteCache<String, HPMagic> magic;

   private Map<Session, ContinuousQueryListener<String, HPMagic>> listeners = new ConcurrentHashMap<>();

   @OnOpen
   public void onOpen(Session session) {
      LOG.info("Session opened");
      ContinuousQuery<String, HPMagic> continuousQuery = Search.getContinuousQuery(magic);

      QueryFactory queryFactory = Search.getQueryFactory(magic);
      Query query = queryFactory.from(HPMagic.class)
            .having("hogwarts").eq(true)
            .build();

      ContinuousQueryListener<String, HPMagic> listener = new ContinuousQueryListener<String, HPMagic>() {
         @Override
         public void resultJoining(String key, HPMagic value) {
            try {
               session.getBasicRemote().sendText(value.getAuthor() + " executed " + value.getSpell());
            } catch (IOException e) {
               LOG.error("Ops, something went wrong...", e);
            }
         }
      };

      continuousQuery.addContinuousQueryListener(query, listener);
      listeners.put(session, listener);
   }

   @OnClose
   public void onClose(Session session) {
      LOG.info("Session closed");
      removeListener(session);
   }

   @OnError
   public void onError(Session session, Throwable throwable) {
      LOG.error("Session ended with error", throwable);
      removeListener(session);
   }

   private void removeListener(Session session) {
      ContinuousQueryListener<String, HPMagic> queryListener = listeners.get(session);
      ContinuousQuery<String, HPMagic> continuousQuery = Search.getContinuousQuery(magic);
      continuousQuery.removeContinuousQueryListener(queryListener);
      listeners.remove(session);
   }
}
