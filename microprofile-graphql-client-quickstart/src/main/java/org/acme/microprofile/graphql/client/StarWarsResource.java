package org.acme.microprofile.graphql.client;


import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.Operation.operation;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.acme.microprofile.graphql.client.model.Film;
import org.acme.microprofile.graphql.client.model.FilmConnection;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.core.Document;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;

/*
    Shows the usage of the typesafe and dynamic GraphQL clients.
    In both cases, the same query is executed against the Star Wars API.
    The query retrieves a list of all films, and for each film, the names of
    all planets which appear in that film.
    The query is:

    {
      allFilms {
        films {
          title
          planetConnection {
            planets {
              name
            }
          }
        }
      }
    }

    You can try executing this query manually on https://graphql.org/swapi-graphql
 */
@Path("/")
public class StarWarsResource {

    // example of typesafe client usage follows

    @Inject
    StarWarsClientApi typesafeClient;

    @GET
    @Path("/typesafe")
    @Blocking
    public List<Film> getAllFilmsUsingTypesafeClient() {
        return typesafeClient.allFilms().getFilms();
    }

    // example of dynamic client usage follows

    @Inject
    @GraphQLClient("star-wars-dynamic")
    DynamicGraphQLClient dynamicClient;

    @GET
    @Path("/dynamic")
    @Blocking
    public List<Film> getAllFilmsUsingDynamicClient() throws Exception {
        Document query = document(
                operation(
                        field("allFilms",
                                field("films",
                                        field("title"),
                                        field("planetConnection",
                                                field("planets",
                                                        field("name")
                                                )
                                        )
                                )
                        )
                )
        );
        Response response = dynamicClient.executeSync(query);
        // Either work with the data as a JsonObject, or, as we show here,
        // translate it into an instance of the corresponding model class
        return response.getObject(FilmConnection.class, "allFilms").getFilms();
    }

}
