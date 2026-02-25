package org.acme;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/movies")
@Tag(name = "movies")
@RunOnVirtualThread
public class MovieResource {

    @GET
    @Operation(summary = "Returns all the movies stored in the database")
    @APIResponse(
            responseCode = "200",
            description = "Gets all movies",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = Movie.class, type = SchemaType.ARRAY),
                    examples = @ExampleObject(name = "movies", value = Examples.VALID_EXAMPLE_MOVIE_LIST)
            )
    )
    public List<Movie> getAll() {
        return Movie.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Returns a movie for a given identifier")
    @APIResponse(
            responseCode = "200",
            description = "Gets a movie for a given id",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = Movie.class),
                    examples = @ExampleObject(name = "movie", value = Examples.VALID_EXAMPLE_MOVIE)
            )
    )
    @APIResponse(
            responseCode = "404",
            description = "The movie is not found for a given identifier"
    )
    public Movie getOne(Long id) {
        Movie movie = Movie.findById(id);
        if (movie == null) {
            throw new NotFoundException("Movie not found");
        }
        return movie;
    }

    @POST
    @Transactional
    @Operation(summary = "Adds a movie and its associated rating")
    @APIResponse(
            responseCode = "201",
            description = "The URI of the created movie",
            headers = @Header(name = HttpHeaders.LOCATION, schema = @Schema(implementation = URI.class))
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid movie passed in (or no request body found)"
    )
    public Response create(
            @RequestBody(
                    name = "movie",
                    content = @Content(
                            mediaType = APPLICATION_JSON,
                            schema = @Schema(implementation = Movie.class),
                            examples = @ExampleObject(name = "movie", value = Examples.VALID_EXAMPLE_MOVIE_TO_CREATE)
                    )
            )
            @Valid @NotNull Movie movie,
            @Context UriInfo uriInfo) {
        movie.persist();
        var builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(movie.id));
        Log.debugf("New movie created with URI %s", builder.build().toString());
        return Response.created(builder.build()).build();
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Updates an exiting movie")
    @APIResponse(
            responseCode = "200",
            description = "Movie updated",
            content = @Content(
                    mediaType = APPLICATION_JSON,
                    schema = @Schema(implementation = Movie.class),
                    examples = @ExampleObject(name = "movie", value = Examples.VALID_EXAMPLE_MOVIE)
            )
    )
    @APIResponse(
            responseCode = "400",
            description = "Invalid movie passed in (or no request body found)")
    @APIResponse(
            responseCode = "404",
            description = "The movie is not found for a given identifier"
    )
    public Movie update(@Parameter(name = "id", required = true) Long id,
                        @RequestBody(
                                name = "movie",
                                content = @Content(
                                        schema = @Schema(implementation = Movie.class),
                                        examples = @ExampleObject(name = "movie", value = Examples.VALID_EXAMPLE_MOVIE)
                                )
                        )
                        @NotNull Movie movie) {
        Movie existing = Movie.findById(id);
        if (existing == null) {
            throw new NotFoundException("Movie not found");
        }
        existing.title = movie.title;
        existing.rating = movie.rating;
        return existing;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Deletes an exiting movie")
    public void delete(@Parameter(name = "id", required = true) Long id) {
        Movie movie = Movie.findById(id);
        if (movie == null) {
            throw new NotFoundException("Movie not found");
        }
        Log.debugf("Movie with id %d deleted ", id);
        movie.delete();
    }

    @DELETE
    @Transactional
    @Operation(summary = "Delete all movies")
    @APIResponse(
            responseCode = "204",
            description = "Deletes all movies"
    )
    public void deleteAll() {
        Movie.deleteAll();
        Log.debug("Deleted all movies");

    }

    @Inject
    AppConfig config;

    @GET
    @Path("/hello")
    @Tag(name = "hello")
    @Operation(summary = "Ping hello")
    @APIResponse(
            responseCode = "200",
            description = "Ping hello",
            content = @Content(
                    schema = @Schema(implementation = String.class),
                    examples = @ExampleObject(name = "hello", value = "Hello Movie Resource")
            )
    )
    public String hello() {
        Log.debugf("Hello Movie Resource - returning %s", config.hello().message());
        return config.hello().message();
    }
}
