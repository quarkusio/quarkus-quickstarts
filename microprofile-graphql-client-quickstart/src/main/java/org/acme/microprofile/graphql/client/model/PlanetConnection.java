package org.acme.microprofile.graphql.client.model;

import java.util.List;

public class PlanetConnection {

    private List<Planet> planets;

    public List<Planet> getPlanets() {
        return planets;
    }

    public void setPlanets(List<Planet> planets) {
        this.planets = planets;
    }

}
