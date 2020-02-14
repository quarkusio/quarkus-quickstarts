package org.acme.api

import org.acme.config.TokenHelper
import org.acme.data.UsersService
import org.acme.models.User
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/users")
class UsersApi(private val usersService: UsersService, private val tokenHelper: TokenHelper) {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    fun save(@Valid user: User): Response {
        if (usersService.isUsernameTaken(user.username)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Username '${user.username}' already taken.").build()
        }
        val result = usersService.save(user)
        return if (result != null) {
            Response .status(Response.Status.CREATED).entity("created $result").build()
        } else Response.status(Response.Status.BAD_REQUEST).entity("unable to create user!").build()
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    fun login(@Valid user: User): Response {
        return if (usersService.authenticate(user.username,user.password)) {
            val token = tokenHelper.getToken(user)
            Response.ok(token).build()
        } else Response.status(Response.Status.BAD_REQUEST).entity("Invalid credentials!").build()
    }
}