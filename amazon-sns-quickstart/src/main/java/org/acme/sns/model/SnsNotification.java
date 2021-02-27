package org.acme.sns.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Based on https://docs.aws.amazon.com/sns/latest/dg/sns-message-and-json-formats.html#http-notification-json
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true) //Required because of https://github.com/localstack/localstack/pull/2181
public class SnsNotification {

    @JsonProperty("Message")
    private String message;

    @JsonProperty("MessageId")
    private String messageId;

    @JsonProperty("Signature")
    private String signature;

    @JsonProperty("SignatureVersion")
    private String signatureVersion;

    @JsonProperty("SigningCertURL")
    private String signinCertUrl;

    @JsonProperty("Subject")
    private String subject;

    @JsonProperty("Timestamp")
    private String timestamp;

    @JsonProperty("TopicArn")
    private String topicArn;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("UnsubscribeURL")
    private String unsubscribeURL;

}
