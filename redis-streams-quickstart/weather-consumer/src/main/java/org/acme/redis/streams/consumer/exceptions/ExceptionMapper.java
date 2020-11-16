package org.acme.redis.streams.consumer.exceptions;

import io.quarkus.vertx.web.Route;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolationException;

import static io.quarkus.vertx.web.Route.HandlerType.FAILURE;

public class ExceptionMapper {

    static final Logger log = LoggerFactory.getLogger(ExceptionMapper.class);

    @Route(path = "/*", type = FAILURE, produces = "application/json", order = 1)
    public void handleApplicationExceptions(ApplicationException e, HttpServerResponse response) {
        response.setStatusCode(e.status).end(new JsonObject()
                .put("status", e.status)
                .put("message", e.getMessage())
                .put("exceptionType", e.getClass().getName())
                .encode());
    }

    @Route(path = "/*", type = FAILURE, produces = "application/json", order = 2)
    public void handleConstraintViolationExceptions(ConstraintViolationException e, HttpServerResponse response) {
        JsonArray result = new JsonArray();

        e.getConstraintViolations().forEach(err ->
                result.add(new JsonObject()
                        .put("invalidValue", err.getInvalidValue())
                        .put("message", err.getMessage())
                )
        );
        response.setStatusCode(400).end(result.encode());
    }
}
