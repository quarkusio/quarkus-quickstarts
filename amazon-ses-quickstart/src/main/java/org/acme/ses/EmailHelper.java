package org.acme.ses;

import lombok.experimental.UtilityClass;
import org.acme.ses.model.Email;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

@UtilityClass
public class EmailHelper {

    public static SendEmailRequest createRequest(Email data) {

        Message message = Message.builder()
                .subject(subject -> subject.data(data.getSubject()))
                .body(body -> body.text(txt -> txt.data(data.getBody())))
                .build();

        return SendEmailRequest.builder()
                .source(data.getFrom())
                .destination(dest -> dest.toAddresses(data.getTo()))
                .message(message)
                .build();
    }
}
