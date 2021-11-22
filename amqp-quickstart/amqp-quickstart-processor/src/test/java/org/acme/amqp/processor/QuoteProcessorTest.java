package org.acme.amqp.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.vertx.amqp.AmqpClientOptions;
import io.vertx.mutiny.amqp.AmqpClient;
import io.vertx.mutiny.amqp.AmqpConnection;
import io.vertx.mutiny.amqp.AmqpMessage;
import io.vertx.mutiny.amqp.AmqpReceiver;
import io.vertx.mutiny.amqp.AmqpSender;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class QuoteProcessorTest {

    @ConfigProperty(name = "amqp-host") String host;
    @ConfigProperty(name = "amqp-port") int port;
    private AmqpClient client;

    @BeforeEach
    void setUp() {
        client = AmqpClient.create(new AmqpClientOptions().setHost(host).setPort(port));
    }

    @AfterEach
    void tearDown() {
        client.closeAndAwait();
    }

    @Test
    void testProcessor() {
        AmqpConnection connection = client.connectAndAwait();
        AmqpReceiver quotes = connection.createReceiverAndAwait("quotes");
        AssertSubscriber<AmqpMessage> subscriber = quotes.toMulti().subscribe().withSubscriber(AssertSubscriber.create(Long.MAX_VALUE));
        AmqpSender sender = connection.createSenderAndAwait("quote-requests");
        UUID quoteId = UUID.randomUUID();
        sender.sendWithAckAndAwait(AmqpMessage.create().address("quote-requests").withBody(quoteId.toString()).build());
        subscriber.awaitItems(1);
        AmqpMessage received = subscriber.getItems().get(0);
        assertEquals(received.bodyAsJsonObject().getString("id"), quoteId.toString());
    }
}
