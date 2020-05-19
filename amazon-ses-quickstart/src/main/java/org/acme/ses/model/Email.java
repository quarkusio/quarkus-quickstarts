package org.acme.ses.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Email {

    private String from;
    private String to;
    private String subject;
    private String body;

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public Email setFrom(String from) {
        this.from = from;
        return this;
    }

    public Email setTo(String to) {
        this.to = to;
        return this;
    }

    public Email setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public Email setBody(String body) {
        this.body = body;
        return this;
    }
}
