package org.acme.redis.streams.consumer.util;

import io.vertx.core.json.Json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class for creating Redis commands.
 */
public final class RedisSupport {

    private static final String GROUP = "group";
    private static final String STREAMS = "streams";
    private static final String CREATE = "create";

    public static List<String> toXReadGroupCommand(String consumerGroup, String threadName, String stream) {
        return toXReadGroupCommand(consumerGroup, threadName, stream, ">");
    }

    public static List<String> toXReadGroupCommand(String consumerGroup, String threadName, String stream, String position) {
        return Arrays.asList(GROUP, consumerGroup, threadName, STREAMS, stream, position);
    }

    public static List<String> toXAckCommand(String stream, String consumerGroup, String messageId) {
        return Arrays.asList(stream, consumerGroup, messageId);
    }

    public static List<String> toXAckCommand(String stream, String consumerGroup, List<String> messageIds) {
        List<String> result = new ArrayList<>();
        result.add(stream);
        result.add(consumerGroup);
        result.addAll(messageIds);
        return result;
    }

    public static List<String> toXGroupCommand(String stream, String consumerGroup) {
        return toXGroupCommand(stream, consumerGroup, "$");
    }

    public static List<String> toXGroupCommand(String stream, String consumerGroup, String position) {
        return Arrays.asList(CREATE, stream, consumerGroup, position);
    }

    public static List<String> toHSetCommand(String key, String field, Object value) {
        return Arrays.asList(key, field, Json.encode(value));
    }
}
