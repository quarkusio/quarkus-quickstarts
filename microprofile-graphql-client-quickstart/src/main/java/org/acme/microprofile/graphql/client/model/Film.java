package org.acme.microprofile.graphql.client.model;

public class Film {
    
    private String title;

    private PlanetConnection planetConnection;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PlanetConnection getPlanetConnection() {
        return planetConnection;
    }

    public void setPlanetConnection(PlanetConnection planetConnection) {
        this.planetConnection = planetConnection;
    }
}
