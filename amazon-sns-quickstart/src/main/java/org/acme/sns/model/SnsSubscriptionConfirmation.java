package org.acme.sns.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Based on https://docs.aws.amazon.com/sns/latest/dg/sns-message-and-json-formats.html#http-subscription-confirmation-json
 */
@Data
public class SnsSubscriptionConfirmation {

    @JsonProperty("Message")
    private String message;

    @JsonProperty("MessageId")
    private String messageId;

    @JsonProperty("Signature")
    private String signature;

    @JsonProperty("SignatureVersion")
    private String signatureVersion;

    @JsonProperty("SigningCertURL")
    private String signingCertUrl;

    @JsonProperty("SubscribeURL")
    private String subscribeUrl;

    @JsonProperty("Timestamp")
    private String timestamp;

    @JsonProperty("Token")
    private String token;

    @JsonProperty("TopicArn")
    private String topicArn;

    @JsonProperty("Type")
    private String type;

}
