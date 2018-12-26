package org.acme.validation;

import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    public Result tryMeManualValidation(Book book) {
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        Result res = new Result();
        if (violations.isEmpty()) {
            res.success = true;
            res.message = "Book is valid! It was validated by manual validation.";
        } else {
            res.success = false;
            res.message = violations.stream()
                .map(cv -> cv.getMessage())
                .collect(Collectors.joining(", "));
        }
        return res;
    }

    @Path("/end-point-method-validation")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Result tryMeEndPointMethodValidation(@Valid Book book) {
        Result res = new Result();
        res.success = true;
        res.message = "Book is valid! It was validated by end point method validation.";
        return res;
    }

    @Path("/service-method-validation")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Result tryMeServiceMethodValidation(Book book) {
        Result res = new Result();

        try {
            bookService.validateBook(book);

            res.success = true;
            res.message = "Book is valid! It was validated by service method validation.";
        }
        catch (ConstraintViolationException e) {
            res.success = false;
            res.message = e.getConstraintViolations().stream()
                .map(cv -> cv.getMessage())
                .collect(Collectors.joining(", "));
        }

        return res;
    }

    public class Result {
        private String message;
        private boolean success;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
