package org.acme.microprofile.graphql.client;

import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;
import org.acme.microprofile.graphql.client.model.FilmConnection;

@GraphQLClientApi(configKey = "star-wars-typesafe")
public interface StarWarsClientApi {

    FilmConnection allFilms();

}
