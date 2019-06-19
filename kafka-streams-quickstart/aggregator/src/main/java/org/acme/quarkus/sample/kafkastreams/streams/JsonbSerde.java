package org.acme.quarkus.sample.kafkastreams.streams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

/**
 * A {@link Serde} that (de-)serializes JSON.
 */
public class JsonbSerde<T> implements Serde<T> {

    private final Class<T> type;
    private final Jsonb jsonb = JsonbBuilder.create();

    public JsonbSerde(Class<T> type) {
        this.type = type;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public void close() {
        try {
            jsonb.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Serializer<T> serializer() {
        return new JsonSerializer<T>();
    }

    @Override
    public Deserializer<T> deserializer() {
        return new JsonDeserializer<T>(type);
    }

    private final class JsonDeserializer<U> implements Deserializer<U> {

        private final Class<U> type;

        public JsonDeserializer(Class<U> type) {
            this.type = type;
        }

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {
        }

        @Override
        public U deserialize(String topic, byte[] data) {
            if (data == null) {
                return null;
            }

            try (InputStream is = new ByteArrayInputStream(data)) {
                return jsonb.fromJson(is, type);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() {
        }
    }

    private final class JsonSerializer<U> implements Serializer<U> {

        @Override
        public void configure(Map<String, ?> configs, boolean isKey) {
        }

        @Override
        public byte[] serialize(String topic, U data) {
            try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
                jsonb.toJson(data, output);
                return output.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() {
        }
    }
}
