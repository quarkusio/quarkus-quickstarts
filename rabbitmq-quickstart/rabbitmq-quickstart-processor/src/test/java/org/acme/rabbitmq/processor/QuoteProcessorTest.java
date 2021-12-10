package org.acme.rabbitmq.processor;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.SECONDS;
import static com.rabbitmq.client.BuiltinExchangeType.TOPIC;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import org.acme.rabbitmq.model.Quote;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class QuoteProcessorTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testProcessor() throws Exception {
        String quoteId = UUID.randomUUID().toString();

        Channel channel = getChannel();

        channel.exchangeDeclare("quotes", TOPIC, true, false, Map.of());

        String queue = channel.queueDeclare("quotes", true, false, false, Map.of())
            .getQueue();
        channel.queueBind(queue, "quotes", "#");

        AtomicReference<Quote> receivedQuote = new AtomicReference<>(null);

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            Quote quote = objectMapper.readValue(message.getBody(), Quote.class);
            if (!Objects.equals(quote.id, quoteId)) {
                return;
            }
            receivedQuote.set(quote);
        };
        String consumerTag = channel.basicConsume(queue, true, deliverCallback, tag -> {});

        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
            .contentType("text/plain")
            .build();
        channel.basicPublish("quote-requests", quoteId, props, quoteId.getBytes(UTF_8));

        await().atMost(3, SECONDS).untilAtomic(receivedQuote, notNullValue());

        channel.basicCancel(consumerTag);
    }

    Channel getChannel() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setChannelRpcTimeout((int) SECONDS.toMillis(3));

        Connection connection = connectionFactory.newConnection();
        return connection.createChannel();
    }
}
