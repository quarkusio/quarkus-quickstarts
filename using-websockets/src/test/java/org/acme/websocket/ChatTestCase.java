package org.acme.websocket;

import org.jboss.shamrock.test.ShamrockTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.net.URI;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@RunWith(ShamrockTest.class)
public class ChatTestCase {

    private static final LinkedBlockingDeque<String> MESSAGES = new LinkedBlockingDeque<>();

    @Test
    public void testWebsocketChat() throws Exception{
        Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, new URI("ws://localhost:8080/chat/stu"));
        Assert.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
        Assert.assertEquals("User stu joined", MESSAGES.poll(10, TimeUnit.SECONDS));
        session.getAsyncRemote().sendText("hello world");
        Assert.assertEquals(">> stu: hello world", MESSAGES.poll(10, TimeUnit.SECONDS));


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
