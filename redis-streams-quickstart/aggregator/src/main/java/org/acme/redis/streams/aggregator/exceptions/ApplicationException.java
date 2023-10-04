package org.acme.redis.streams.aggregator.exceptions;

import static java.lang.String.format;

public class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = -3674046621671906553L;
    public int status;

    public ApplicationException(int status, String message, Object... args) {
        super(format(message, args));
        this.status = status;
    }

    public ApplicationException(String message, Object... args) {
        super(format(message, args));
        this.status = 400;
    }
}