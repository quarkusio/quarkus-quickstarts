package org.acme.quickstart;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import javax.transaction.Status;

@QuarkusTest
public class TransactionalResourceTest {
    private final String NO_TXN = String.valueOf(Status.STATUS_NO_TRANSACTION);
    private final String ACTIVE = String.valueOf(Status.STATUS_ACTIVE);
    private final String COMMITTED = String.valueOf(Status.STATUS_COMMITTED);

    @Test
    public void testEndpoint() {
        given()
                .when().get("/jta")
                .then()
                .statusCode(200)
                .body(is(NO_TXN));
    }

    @Test
    public void testCMTEndpoint() {
        given()
                .when().post("/jta/cmt")
                .then()
                .statusCode(200)
                .body(is(ACTIVE));
    }

    @Test
    public void testBMTEndpoint() {
        given()
                .when().post("/jta/bmt")
                .then()
                .statusCode(200)
                .body(is(NO_TXN));
    }

    @Test
    @Disabled("This is broken with RESTEasy Reactive")
    public void testAsync1Endpoint() {
        given()
                .when().post("/jta/async-with-suspended")
                .then()
                .statusCode(200)
                .body(is(COMMITTED));
    }

    @Test
    public void testAsync2Endpoint() {
        given()
                .when().post("/jta/async-with-completion-stage")
                .then()
                .statusCode(200)
                // the resource method returns the transaction status from the async thread and is should be active
                .body(is(ACTIVE));
    }

//    @Test // this test is a reproducer for quarkus issue 6471. Enable it when it gets fixed
    public void testAsync3Endpoint() {
        given()
                .when().post("/jta/async-6471-reproducer")
                .then()
                .statusCode(200)
                // the resource method returns the transaction status in an AsyncResponse resume
                // ar.resume(Response.ok().entity(getTransactionStatus()).build())
                .body(is(ACTIVE));
    }
}
