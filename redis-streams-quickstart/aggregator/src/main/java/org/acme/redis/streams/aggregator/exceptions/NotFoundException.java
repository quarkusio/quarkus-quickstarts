package org.acme.redis.streams.aggregator.exceptions;


import java.util.function.Supplier;

public class NotFoundException extends ApplicationException {

    private static final long serialVersionUID = -3937380385452268477L;

    public NotFoundException(String msg, Object... args) {
        super(404, msg, args);
    }
}