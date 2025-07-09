package org.acme.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.client.OidcTestClient;
import io.vertx.core.Vertx;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketClient;
import io.vertx.core.http.WebSocketConnectOptions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class WebSocketsOidcIdentityUpdateTest {

    private static volatile Vertx vertx = null;
    private static volatile OidcTestClient oidcTestClient;

    @TestHTTPResource("/chat")
    URI chatUri;

    @BeforeAll
    public static void beforeAll() {
        vertx = Vertx.vertx();
        oidcTestClient = new OidcTestClient();
    }

    @AfterAll
    public static void afterAll() {
        if (vertx != null) {
            vertx.close().toCompletionStage().toCompletableFuture().join();
        }
        if (oidcTestClient != null) {
            oidcTestClient.close();
        }
    }

    @Test
    public void testIdentityUpdate() throws InterruptedException, ExecutionException, TimeoutException, JsonProcessingException {
        CountDownLatch connectedLatch = new CountDownLatch(1);
        CountDownLatch messagesLatch = new CountDownLatch(2);
        List<String> messages = new CopyOnWriteArrayList<>();
        AtomicReference<WebSocket> ws1 = new AtomicReference<>();
        WebSocketClient client = vertx.createWebSocketClient();
        WebSocketConnectOptions options = new WebSocketConnectOptions();
        options.setHost(chatUri.getHost());
        options.setPort(chatUri.getPort());
        options.setURI(chatUri.getPath() + "/IF");
        options.setSubProtocols(
                List.of("quarkus",
                        "quarkus-http-upgrade#Authorization#Bearer " + oidcTestClient.getAccessToken("alice", "alice")));
        try {
            client
                    .connect(options)
                    .onComplete(r -> {
                        if (r.succeeded()) {
                            WebSocket ws = r.result();
                            ws.textMessageHandler(msg -> {
                                messages.add(msg);
                                messagesLatch.countDown();
                            });
                            // We will use this socket to write a message later on
                            ws1.set(ws);
                            connectedLatch.countDown();
                        } else {
                            throw new IllegalStateException(r.cause());
                        }
                    });
            assertTrue(connectedLatch.await(5, TimeUnit.SECONDS));
            ws1.get().writeTextMessage(createChatMessage("hi"));
            assertTrue(messagesLatch.await(5, TimeUnit.SECONDS), "Messages: " + messages);
            assertEquals(2, messages.size(), "Messages: " + messages);
            assertTrue(messages.get(0).contains("USER_JOINED"), messages.get(0));
            var objectMapper = new ObjectMapper();
            var message = objectMapper.readValue(messages.get(1), ChatWebSocket.ChatMessage.class);
            assertNotNull(message);
            assertEquals(ChatWebSocket.MessageType.CHAT_MESSAGE, message.type());
            assertEquals("alice", message.from());
            assertEquals("hi", message.message());
            long expiresAt = message.expiresAt();
            Thread.sleep(1001);
            ws1.get().writeTextMessage("""
                    {
                        "metadata": {
                            "token": "%s"
                        }
                    }
                    """.formatted(oidcTestClient.getAccessToken("alice", "alice")));
            Awaitility.await().atMost(Duration.ofSeconds(15)).untilAsserted(() -> {
                ws1.get().writeTextMessage(createChatMessage("hey"));
                var lastMessage = objectMapper.readValue(messages.get(messages.size() - 1), ChatWebSocket.ChatMessage.class);
                assertTrue(lastMessage.expiresAt() > expiresAt);
            });
        } finally {
            client.close().toCompletionStage().toCompletableFuture().get(5, TimeUnit.SECONDS);
        }
    }

    private static String createChatMessage(String message) {
        return """
                {
                    "chatMessage": {
                        "type": "CHAT_MESSAGE",
                        "from": "alice",
                        "message": "%s"
                    }
                }
                """.formatted(message);
    }

}
