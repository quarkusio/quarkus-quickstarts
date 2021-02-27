package org.acme.sns;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import lombok.extern.jbosslog.JBossLog;
import org.acme.sns.model.Quark;
import org.acme.sns.model.SnsNotification;
import org.acme.sns.model.SnsSubscriptionConfirmation;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@JBossLog
@Path("/sync/shield")
public class QuarksShieldSyncResource {

    private static final String NOTIFICATION_TYPE = "Notification";
    private static final String SUBSCRIPTION_CONFIRMATION_TYPE = "SubscriptionConfirmation";
    private static final String UNSUBSCRIPTION_CONFIRMATION_TYPE = "UnsubscribeConfirmation";

    @Inject
    SnsClient sns;

    @ConfigProperty(name = "topic.arn")
    String topicArn;

    @ConfigProperty(name = "quarks.shield.base.url")
    String quarksShieldBaseUrl;

    private volatile String subscriptionArn;

    static Map<Class<?>, ObjectReader> READERS = new HashMap<>();

    static {
        READERS.put(SnsNotification.class, new ObjectMapper().readerFor(SnsNotification.class));
        READERS.put(SnsSubscriptionConfirmation.class, new ObjectMapper().readerFor(SnsSubscriptionConfirmation.class));
        READERS.put(Quark.class, new ObjectMapper().readerFor(Quark.class));
    }

    @POST
    @Consumes({TEXT_PLAIN})
    public Response notificationEndpoint(@HeaderParam("x-amz-sns-message-type") String messageType, String message) {
        if (messageType == null) {
            return Response.status(400).build();
        }

        switch (messageType) {
            case NOTIFICATION_TYPE:
                SnsNotification notification = readObject(SnsNotification.class, message);
                Quark quark = readObject(Quark.class, notification.getMessage());
                log.infov("Quark[{0}, {1}] collision with the shield.", quark.getFlavor(), quark.getSpin());
                break;
            case SUBSCRIPTION_CONFIRMATION_TYPE:
                SnsSubscriptionConfirmation subConf = readObject(SnsSubscriptionConfirmation.class, message);
                sns.confirmSubscription(cs -> cs.topicArn(topicArn).token(subConf.getToken()));
                log.info("Subscription confirmed. Ready for quarks collisions.");
                break;
            case UNSUBSCRIPTION_CONFIRMATION_TYPE:
                log.info("We are unsubscribed");
                break;
            default:
                return Response.status(400).entity("Unknown messageType").build();
        }

        return Response.ok().build();
    }

    @POST
    @Path("subscribe")
    public Response subscribe() {
        String notificationEndpoint = notificationEndpoint();
        SubscribeResponse response = sns.subscribe(s -> s.topicArn(topicArn).protocol("http").endpoint(notificationEndpoint));
        subscriptionArn = response.subscriptionArn();
        log.infov("Subscribed Quarks shield <{0}> : {1} ", notificationEndpoint, response.subscriptionArn());
        return Response.ok().entity(response.subscriptionArn()).build();
    }

    @POST
    @Path("unsubscribe")
    public Response unsubscribe() {
        if (subscriptionArn != null) {
            sns.unsubscribe(s -> s.subscriptionArn(subscriptionArn));
            log.infov("Unsubscribed quarks shield for id = {0}", subscriptionArn);
            return Response.ok().build();
        } else {
            log.info("Not subscribed yet");
            return Response.status(400).entity("Not subscribed yet").build();
        }
    }

    private String notificationEndpoint() {
        return quarksShieldBaseUrl + "/sync/shield";
    }

    private <T> T readObject(Class<T> clazz, String message) {
        T object = null;
        try {
            object = READERS.get(clazz).readValue(message);
        } catch (JsonProcessingException e) {
            log.errorv("Unable to deserialize message <{0}> to Class <{1}>", message, clazz.getSimpleName());
            throw new RuntimeException(e);
        }
        return object;
    }
}
