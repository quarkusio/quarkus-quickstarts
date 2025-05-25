package org.acme.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.websockets.next.TextMessageCodec;
import io.quarkus.websockets.next.WebSocketSecurity;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.lang.reflect.Type;

@Priority(15)
@Singleton
public class ChatMessageDecoder implements TextMessageCodec<ChatWebSocket.ChatMessage> {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    WebSocketSecurity webSocketSecurity;

    @RegisterForReflection
    private record Metadata(String token) {}
    @RegisterForReflection
    private record Dto(ChatWebSocket.ChatMessage chatMessage, Metadata metadata) {}

    @Override
    public boolean supports(Type type) {
        return type.equals(ChatWebSocket.ChatMessage.class);
    }

    @Override
    public String encode(ChatWebSocket.ChatMessage value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ChatWebSocket.ChatMessage decode(Type type, String value) {
        try {
            var dto = objectMapper.readValue(value, Dto.class);
            if (dto.metadata != null && dto.metadata.token != null) {
                Log.info("Received message with token");
                webSocketSecurity
                        .updateSecurityIdentity(dto.metadata.token)
                        .whenComplete((securityIdentity, throwable) -> {
                            if (throwable != null) {
                                Log.error("Error updating identity", throwable);
                            } else {
                                Log.info("Identity updated");
                            }
                        });
            }
            return dto.chatMessage;
        } catch (JsonProcessingException e) {
            Log.info("Failed to decode message", e);
            throw new RuntimeException(e);
        }
    }
}