package org.acme.optaplanner.rest;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.optaplanner.core.api.solver.SolverStatus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class TimeTableResourceTest {

    @Test
    public void solveDemoDataUntilFeasible() throws InterruptedException {
        given()
            .contentType(ContentType.JSON)
            .when().post("/timeTable/solve")
            .then()
            .statusCode(204);

        await()
                .atMost(Duration.ofMinutes(1))
                .pollDelay(Duration.ofSeconds(5))
                .pollInterval(Duration.ofSeconds(5))
                .until(() -> SolverStatus.NOT_SOLVING.name().equals(get("/timeTable").body().path("solverStatus")));

        get("/timeTable").then().assertThat()
                .body("solverStatus", equalTo(SolverStatus.NOT_SOLVING.name()))
                .body("timeslotList", is(not(empty())))
                .body("roomList", is(not(empty())))
                .body("lessonList", is(not(empty())))
                .body("lessonList.timeslot", not(nullValue()))
                .body("lessonList.room", not(nullValue()));
    }
}
