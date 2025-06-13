package org.acme.websockets;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;

import static io.quarkus.websockets.next.runtime.SecuritySupport.QUARKUS_IDENTITY_EXPIRE_TIME;

@Authenticated
@WebSocket(path = "/chat/{username}")
public class ChatWebSocket {

    public enum MessageType {USER_JOINED, USER_LEFT, CHAT_MESSAGE}

    public record ChatMessage(MessageType type, String from, String message, Long expiresAt) {
    }

    @Inject
    WebSocketConnection connection;

    @Inject
    SecurityIdentity securityIdentity;

    @OnOpen(broadcast = true)
    public ChatMessage onOpen() {
        return new ChatMessage(MessageType.USER_JOINED, connection.pathParam("username"), null, null);
    }

    @OnClose
    public void onClose() {
        ChatMessage departure = new ChatMessage(MessageType.USER_LEFT, connection.pathParam("username"), null, null);
        connection.broadcast().sendTextAndAwait(departure);
    }

    @OnTextMessage(broadcast = true)
    public Multi<ChatMessage> onMessage(ChatMessage message) {
        if (message == null) {
            return Multi.createFrom().empty();
        }
        Long expiresAt = securityIdentity.getAttribute(QUARKUS_IDENTITY_EXPIRE_TIME);
        return Multi.createFrom().item(new ChatMessage(message.type, message.from, message.message, expiresAt));
    }

}