package org.acme.restclient

import io.quarkus.test.junit.QuarkusTest
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@QuarkusTest
open class CountriesServiceTest {

    @Inject
    @field: RestClient
    lateinit var countriesService: CountriesService

    @Test
    fun `get Greece info`() {
        // when
        val countries = countriesService.getByName("Greece")

        // then
        assertEquals(countries.size, 1)
        assertEquals(countries.firstOrNull()?.capital, "Athens")
    }

    @Test
    fun `get Greece info, with async function`() {
        // when
        val countries = countriesService.getByNameAsync("Greece")

        // then
        countries.thenAccept {
            assertEquals(it.size, 1)
            assertEquals(it.firstOrNull()?.capital, "Athens")
        }.toCompletableFuture().get(2, TimeUnit.SECONDS)
    }
}