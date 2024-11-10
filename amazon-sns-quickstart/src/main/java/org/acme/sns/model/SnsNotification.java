package org.acme.sns.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Based on https://docs.aws.amazon.com/sns/latest/dg/sns-message-and-json-formats.html#http-notification-json
 */
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

    public String getSigninCertUrl() {
        return signinCertUrl;
    }

    public void setSigninCertUrl(String signinCertUrl) {
        this.signinCertUrl = signinCertUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public String getUnsubscribeURL() {
        return unsubscribeURL;
    }

    public void setUnsubscribeURL(String unsubscribeURL) {
        this.unsubscribeURL = unsubscribeURL;
    }
}
