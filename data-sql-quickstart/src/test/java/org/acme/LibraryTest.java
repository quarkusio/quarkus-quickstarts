package org.acme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class LibraryTest {

    @Inject
    Library library;

    @Test
    void shouldFindBookByTitle() {
        List<Library.Book> books = library.findByTitle("Effective Java");
        assertEquals(1, books.size());
        assertEquals("Effective Java", books.get(0).title());
    }

    @Test
    void shouldReturnEmptyListForUnknownTitle() {
        List<Library.Book> books = library.findByTitle("Unknown Book");
        assertTrue(books.isEmpty());
    }

    @Test
    @TestTransaction
    void shouldUpdateBookPrice() {
        BigDecimal newPrice = new BigDecimal("39.00");
        int updated = library.updatePrice("978-0134685991", newPrice);
        assertEquals(1, updated);

        List<Library.Book> books = library.findByTitle("Effective Java");
        assertEquals(1, books.size());
        assertEquals(newPrice, books.get(0).price());
    }
}
