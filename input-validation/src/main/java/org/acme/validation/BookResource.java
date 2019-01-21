package org.acme.validation;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/books")
public class BookResource {

    @Inject
    Validator validator;

    @Inject
    BookService bookService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    @Path("/manual-validation")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response tryMeManualValidation(Book book) {
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        if (violations.isEmpty()) {
            return Response.ok(new Result("Book is valid! It was validated by manual validation.")).build();
        } else {
            return Response.status(400).entity(new Result(violations)).build();
        }
    }

    @Path("/end-point-method-validation")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Result tryMeEndPointMethodValidation(@Valid Book book) {
        return new Result("Book is valid! It was validated by end point method validation.");
    }

    @Path("/service-method-validation")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Result tryMeServiceMethodValidation(Book book) {
        bookService.validateBook(book);
        return new Result("Book is valid! It was validated by service method validation.");
    }
}
