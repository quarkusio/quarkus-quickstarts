package org.acme.validation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;

@ApplicationScoped
public class BookService {

    public void validateBook(@Valid Book book) {
        // your business logic here
    }
}
