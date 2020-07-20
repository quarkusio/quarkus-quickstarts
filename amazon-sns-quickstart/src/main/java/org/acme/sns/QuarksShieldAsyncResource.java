package org.acme.sns;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import io.smallrye.mutiny.Uni;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.acme.sns.model.Quark;
import org.acme.sns.model.SnsNotification;
import org.acme.sns.model.SnsSubscriptionConfirmation;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import software.amazon.awssdk.services.sns.SnsAsyncClient;
import software.amazon.awssdk.services.sns.model.SubscribeResponse;

@Path("/async/shield")
public class QuarksShieldAsyncResource {

    private static final Logger LOGGER = Logger.getLogger(QuarksShieldAsyncResource.class);

    private static final String NOTIFICATION_TYPE = "Notification";
    private static final String SUBSCRIPTION_CONFIRMATION_TYPE = "SubscriptionConfirmation";
    private static final String UNSUBSCRIPTION_CONFIRMATION_TYPE = "UnsubscribeConfirmation";

    @Inject
    SnsAsyncClient sns;

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
    @Consumes({MediaType.TEXT_PLAIN})
    public Uni<Response> notificationEndpoint(@HeaderParam("x-amz-sns-message-type") String messageType, String message) {
        if (messageType == null) {
            return Uni.createFrom().item(Response.status(400).build());
        }

        if (messageType.equals(NOTIFICATION_TYPE)) {
            return Uni.createFrom().item(readObject(SnsNotification.class, message))
                .onItem().transform(notification -> readObject(Quark.class, notification.getMessage()))
                .onItem().invoke(quark -> LOGGER.infov("Quark[{0}, {1}] collision with the shield.", quark.getFlavor(), quark.getSpin()))
                .onItem().transform(quark -> Response.ok().build());
        } else if (messageType.equals(SUBSCRIPTION_CONFIRMATION_TYPE)) {
            return Uni.createFrom().item(readObject(SnsSubscriptionConfirmation.class, message))
                .onItem().transformToUni(msg ->
                            Uni.createFrom().completionStage(
                                    sns.confirmSubscription(confirm -> confirm.topicArn(topicArn).token(msg.getToken()))
                            )
                    )
                .onItem().invoke(resp -> LOGGER.info("Subscription confirmed. Ready for quarks collisions."))
                .onItem().transform(resp -> Response.ok().build());
        } else if (messageType.equals(UNSUBSCRIPTION_CONFIRMATION_TYPE)) {
            LOGGER.info("We are unsubscribed");
            return Uni.createFrom().item(Response.ok().build());
        }

        return Uni.createFrom().item(Response.status(400).entity("Unknown messageType").build());
    }

    @POST
    @Path("/subscribe")
    public Uni<Response> subscribe() {
        return Uni.createFrom()
            .completionStage(sns.subscribe(s -> s.topicArn(topicArn).protocol("http").endpoint(notificationEndpoint())))
            .onItem().transform(SubscribeResponse::subscriptionArn)
            .onItem().invoke(this::setSubscriptionArn)
            .onItem().invoke(arn -> LOGGER.infov("Subscribed Quarks shield with id = {0} ", arn))
            .onItem().transform(arn -> Response.ok().entity(arn).build());
    }

    @POST
    @Path("/unsubscribe")
    public Uni<Response> unsubscribe() {
        if (subscriptionArn != null) {
            return Uni.createFrom()
                .completionStage(sns.unsubscribe(s -> s.subscriptionArn(subscriptionArn)))
                .onItem().invoke(arn -> LOGGER.infov("Unsubscribed quarks shield for id = {0}", subscriptionArn))
                .onItem().invoke(arn -> subscriptionArn = null)
                .onItem().transform(arn -> Response.ok().build());
        } else {
            return Uni.createFrom().item(Response.status(400).entity("Not subscribed yet").build());
        }
    }

    private String notificationEndpoint() {
        return quarksShieldBaseUrl + "/async/shield";
    }

    private void setSubscriptionArn(String arn) {
        this.subscriptionArn = arn;
    }

    private <T> T readObject(Class<T> clazz, String message) {
        T object = null;
        try {
            object = READERS.get(clazz).readValue(message);
        } catch (JsonProcessingException e) {
            LOGGER.errorv("Unable to deserialize message <{0}> to Class <{1}>", message, clazz.getSimpleName());
            throw new RuntimeException(e);
        }
        return object;
    }
}