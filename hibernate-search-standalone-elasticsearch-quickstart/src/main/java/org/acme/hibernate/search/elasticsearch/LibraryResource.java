package org.acme.hibernate.search.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

import org.acme.hibernate.search.elasticsearch.model.Author;
import org.acme.hibernate.search.elasticsearch.model.Book;

import org.hibernate.search.mapper.pojo.standalone.mapping.SearchMapping;
import org.hibernate.search.mapper.pojo.standalone.session.SearchSession;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import io.quarkus.runtime.StartupEvent;

@Path("/library")
public class LibraryResource {

    @Inject
    SearchMapping searchMapping;

    void onStart(@Observes StartupEvent ev) {
        // Index some test data if nothing exists
        try (var searchSession = searchMapping.createSession()) {
            if (0 < searchSession.search(Author.class)
                    .where(f -> f.matchAll())
                    .fetchTotalHitCount()) {
                return;
            }
            for (Author author : initialDataSet()) {
                searchSession.indexingPlan().add(author);
            }
        }
    }

    private List<Author> initialDataSet() {
        return List.of(
                new Author(UUID.randomUUID(), "John", "Irving",
                        List.of(
                                new Book(UUID.randomUUID(), "The World According to Garp"),
                                new Book(UUID.randomUUID(), "The Hotel New Hampshire"),
                                new Book(UUID.randomUUID(), "The Cider House Rules"),
                                new Book(UUID.randomUUID(), "A Prayer for Owen Meany"),
                                new Book(UUID.randomUUID(), "Last Night in Twisted River"),
                                new Book(UUID.randomUUID(), "In One Person"),
                                new Book(UUID.randomUUID(), "Avenue of Mysteries"))),
                new Author(UUID.randomUUID(), "Paul", "Auster",
                        List.of(
                                new Book(UUID.randomUUID(), "The New York Trilogy"),
                                new Book(UUID.randomUUID(), "Mr. Vertigo"),
                                new Book(UUID.randomUUID(), "The Brooklyn Follies"),
                                new Book(UUID.randomUUID(), "Invisible"),
                                new Book(UUID.randomUUID(), "Sunset Park"),
                                new Book(UUID.randomUUID(), "4 3 2 1"))));
    }

    @PUT
    @Path("author")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addAuthor(@RestForm String firstName, @RestForm String lastName) {
        try (var searchSession = searchMapping.createSession()) {
            Author author = new Author(UUID.randomUUID(), firstName, lastName, new ArrayList<>());
            searchSession.indexingPlan().add(author);
        }
    }

    @GET
    @Path("author/{id}")
    public Author getAuthor(@RestPath UUID id) {
        try (var searchSession = searchMapping.createSession()) {
            return getAuthor(searchSession, id);
        }
    }

    private Author getAuthor(SearchSession searchSession, UUID id) {
        return searchSession.search(Author.class)
                .where(f -> f.id().matching(id))
                .fetchSingleHit()
                .orElseThrow(NotFoundException::new);
    }

    @POST
    @Path("author/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void updateAuthor(@RestPath UUID id, @RestForm String firstName, @RestForm String lastName) {
        try (var searchSession = searchMapping.createSession()) {
            Author author = getAuthor(searchSession, id);
            author.firstName = firstName;
            author.lastName = lastName;
            searchSession.indexingPlan().addOrUpdate(author);
        }
    }

    @DELETE
    @Path("author/{id}")
    public void deleteAuthor(@RestPath UUID id) {
        try (var searchSession = searchMapping.createSession()) {
            searchSession.indexingPlan().purge(Author.class, id, null);
        }
    }

    @PUT
    @Path("author/{authorId}/book/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addBook(@RestPath UUID authorId, @RestForm String title) {
        try (var searchSession = searchMapping.createSession()) {
            Author author = getAuthor(searchSession, authorId);
            author.books.add(new Book(authorId, title));
            searchSession.indexingPlan().addOrUpdate(author);
        }
    }

    @DELETE
    @Path("author/{authorId}/book/{bookId}")
    public void deleteBook(@RestPath UUID authorId, @RestPath UUID bookId) {
        try (var searchSession = searchMapping.createSession()) {
            Author author = getAuthor(searchSession, authorId);
            author.books.removeIf(book -> book.id.equals(bookId));
            searchSession.indexingPlan().addOrUpdate(author);
        }
    }

    @GET
    @Path("author/search")
    public List<Author> searchAuthors(@RestQuery String pattern,
            @RestQuery Optional<Integer> size) {
        try (var searchSession = searchMapping.createSession()) {
            return searchSession.search(Author.class)
                    .where(f -> pattern == null || pattern.isBlank()
                            ? f.matchAll()
                            : f.simpleQueryString()
                                    .fields("firstName", "lastName", "books.title").matching(pattern))
                    .sort(f -> f.field("lastName_sort").then().field("firstName_sort"))
                    .fetchHits(size.orElse(20));
        }
    }
}
