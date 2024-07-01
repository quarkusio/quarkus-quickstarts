package org.acme.quickstart;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class JdbcObjectStoreTest {

    @BeforeEach
    public void prepareTest() {
        cleanObjectStoreHistoricalData();
    }

    @Test
    public void testTransactionLogsWithTransactionalAnnotation() {
        testTransactionLogs("annotation");
    }

    @Test
    public void testTransactionLogsWithStaticTransactionManager() {
        testTransactionLogs("static-transaction-manager");
    }

    @Test
    public void testTransactionsLogWithInjectedTransactionManager() {
        testTransactionLogs("injected-transaction-manager");
    }

    @Test
    public void testTransactionsLogWithInjectedUserTransaction() {
        testTransactionLogs("injected-user-transaction");
    }

    @Test
    public void testTransactionsLogWithStaticUserTransaction() {
        testTransactionLogs("static-user-transaction");
    }

    private static void makeTransactionAndRollback(String beginTransactionWith) {
        makeTransaction(beginTransactionWith, true);
    }

    private static void makeTransaction(String beginTransactionWith) {
        makeTransaction(beginTransactionWith, false);
    }

    private static void makeTransaction(String beginTransactionWith, boolean rollback) {
        given()
                .when()
                .queryParam("rollback", rollback)
                .get("/transaction-logs/" + beginTransactionWith + "-way")
                .then()
                .statusCode(200)
                .body(Matchers.is("true"));
    }

    private static void assureTransactionLogWasUsed() {
        given()
                .when().get("/transaction-logs/historical-data")
                .then()
                .statusCode(200)
                .body(Matchers.notNullValue());
    }

    private static void assureTransactionLogWasNotUsed() {
        given()
                .when().get("/transaction-logs/historical-data")
                .then()
                .statusCode(204);
    }

    private static void cleanObjectStoreHistoricalData() {
        RestAssured
                .delete("/transaction-logs")
                .then()
                .statusCode(204);
    }

    private static void testTransactionLogs(String beginTransactionWith) {
        makeTransaction(beginTransactionWith);
        assureTransactionLogWasUsed();
        cleanObjectStoreHistoricalData();
        makeTransactionAndRollback(beginTransactionWith);
        assureTransactionLogWasNotUsed();
    }
}
