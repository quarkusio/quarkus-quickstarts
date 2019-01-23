package org.acme.websocket;

import java.net.URI;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.jboss.shamrock.test.junit.ShamrockTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@ShamrockTest
public class ChatTestCase {

    private static final LinkedBlockingDeque<String> MESSAGES = new LinkedBlockingDeque<>();

    @Test
    public void testWebsocketChat() throws Exception {
        Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, new URI("ws://localhost:8080/chat/stu"));
        Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
        Assertions.assertEquals("User stu joined", MESSAGES.poll(10, TimeUnit.SECONDS));
        session.getAsyncRemote().sendText("hello world");
        Assertions.assertEquals(">> stu: hello world", MESSAGES.poll(10, TimeUnit.SECONDS));


    }

    @ClientEndpoint
    public static class Client {

        @OnOpen
        public void open() {
            MESSAGES.add("CONNECT");
        }

        @OnMessage
        void message(String msg) {
            MESSAGES.add(msg);
        }

    }

}
