package org.acme;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.query.NativeQuery;

public interface Library {

    record Book(Long id, String title, String isbn, BigDecimal price) {
    }

    @NativeQuery("SELECT id, title, isbn, price FROM Book WHERE title = :title")
    List<Book> findByTitle(String title);

    @NativeQuery("UPDATE Book SET price = :price WHERE isbn = :isbn")
    int updatePrice(String isbn, BigDecimal price);
}
