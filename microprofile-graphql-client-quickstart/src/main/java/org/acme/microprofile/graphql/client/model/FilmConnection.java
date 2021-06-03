package org.acme.microprofile.graphql.client.model;

import java.util.List;

public class FilmConnection {

    private List<Film> films;

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }
}
