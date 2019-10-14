package org.acme.config

import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/greeting")
class GreetingResource {
    @ConfigProperty(name = "greeting.message")
    lateinit var message: String
    @ConfigProperty(name = "greeting.suffix", defaultValue = "!")
    lateinit var suffix: String
    @ConfigProperty(name = "greeting.name")
    var name: String? = null

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): String {
        return "$message ${name ?: "world"}$suffix"
    }
}