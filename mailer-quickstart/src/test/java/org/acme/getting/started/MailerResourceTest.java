package org.acme.getting.started;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class MailerResourceTest {

    private static final String TO = "foo@quarkus.io";

    @Inject
    MockMailbox mailbox;

    @BeforeEach
    void init() {
        mailbox.clear();
    }

    @Test
    void testMail() {
        given()
                .queryParam("name", "foo")
                .queryParam("email", TO)
                .when()
                .get("/mail")
                .then()
                .statusCode(202);

        // verify that it was sent
        List<Mail> sent = mailbox.getMessagesSentTo(TO);
        assertThat(sent).hasSize(1);
        Mail actual = sent.get(0);
        assertThat(actual.getHtml()).contains("Welcome on board foo!");
        assertThat(actual.getSubject()).isEqualTo("Ahoy foo!");

        assertThat(mailbox.getTotalMessagesSent()).isEqualTo(1);
    }

    @Test
    void testInvalidMail() {
        given()
                .queryParam("name", "foo")
                .queryParam("email", "not-an-email")
                .when()
                .get("/mail")
                .then()
                .statusCode(400);

        assertThat(mailbox.getTotalMessagesSent()).isEqualTo(0);
    }

    @Test
    void testInvalidName() {
        given()
                .queryParam("email", TO)
                .when()
                .get("/mail")
                .then()
                .statusCode(400);

        assertThat(mailbox.getTotalMessagesSent()).isEqualTo(0);
    }
}