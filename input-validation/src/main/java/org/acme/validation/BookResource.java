package org.acme.validation;

import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
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

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Result tryMe(Book book) {
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        Result res = new Result();
        if (violations.isEmpty()) {            
            res.success = true;
            res.message = "woohoo!";
        } else {
            res.success = false;
            res.message = violations.stream()
                .map(cv -> cv.getMessage())
                .collect(Collectors.joining(", "));
        }
        return res;
    }


    private class Result {
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
