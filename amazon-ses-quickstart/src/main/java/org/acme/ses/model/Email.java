package org.acme.ses.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class Email {

    private String from;
    private String to;
    private String subject;
    private String body;


    public Email setBody(String body) {
        this.body = body;
        return this;
    }
}
