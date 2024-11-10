package org.acme.sns.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Based on https://docs.aws.amazon.com/sns/latest/dg/sns-message-and-json-formats.html#http-subscription-confirmation-json
 */
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignatureVersion() {
        return signatureVersion;
    }

    public void setSignatureVersion(String signatureVersion) {
        this.signatureVersion = signatureVersion;
    }

    public String getSigningCertUrl() {
        return signingCertUrl;
    }

    public void setSigningCertUrl(String signingCertUrl) {
        this.signingCertUrl = signingCertUrl;
    }

    public String getSubscribeUrl() {
        return subscribeUrl;
    }

    public void setSubscribeUrl(String subscribeUrl) {
        this.subscribeUrl = subscribeUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTopicArn() {
        return topicArn;
    }

    public void setTopicArn(String topicArn) {
        this.topicArn = topicArn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}