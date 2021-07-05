package org.acme.restclient

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
open class CountriesResourceTest {

    @Test
    fun `get Greece country info`() {
        given()
                .`when`().get("/country/name/greece")
                .then()
                .statusCode(200)
                .body("$.size()", `is`(1),
                        "[0].alpha2Code", `is`("GR"),
                        "[0].capital", `is`("Athens"),
                        "[0].currencies.size()", `is`(1),
                        "[0].currencies[0].name", `is`("Euro")
                )
    }

    @Test
    fun `get Greece country info, with async endpoint`() {
        given()
                .`when`().get("/country/name-async/greece")
                .then()
                .statusCode(200)
                .body("$.size()", `is`(1),
                        "[0].alpha2Code", `is`("GR"),
                        "[0].capital", `is`("Athens"),
                        "[0].currencies.size()", `is`(1),
                        "[0].currencies[0].name", `is`("Euro")
                )
    }

}