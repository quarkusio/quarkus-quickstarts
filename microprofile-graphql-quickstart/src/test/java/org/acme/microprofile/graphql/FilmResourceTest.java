package org.acme.microprofile.graphql;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
public class FilmResourceTest {

    @Test
    public void allFilms() {

        String requestBody =
                "{\"query\":" +
                        "\"" +
                            "{" +
                                " allFilms  {" +
                                    " title" +
                                    " director" +
                                    " releaseDate" +
                                    " episodeID" +
                                  "}" +
                            "}" +
                        "\"" +
                "}";

        given()
                .body(requestBody)
                .post("/graphql/")
                .then()
                .contentType(ContentType.JSON)
                .body("data.allFilms.size()", is(3))
                .body("data.allFilms.director", hasItem("George Lucas"))
                .statusCode(200);
    }

    @Test
    public void getFilm() {

        String requestBody =
                "{\"query\":" +
                        "\"" +
                            "{" +
                                "film (filmId: 1)  {" +
                                    " title" +
                                    " director" +
                                    " releaseDate" +
                                    " episodeID" +
                                "}" +
                            "}" +
                        "\"" +
                "}";

        given()
                .body(requestBody)
                .post("/graphql/")
                .then()
                .contentType(ContentType.JSON)
                .body("data.size()", is(1))
                .body("data.film.director", is("George Lucas"))
                .statusCode(200);
    }

    @Test
    public void getFilmWithHero() {

        String requestBody =
                "{\"query\":" +
                        "\"" +
                            "{" +
                                "film (filmId: 0)  {" +
                                    " title" +
                                    " director" +
                                    " releaseDate" +
                                    " episodeID" +
                                    " heroes { " +
                                        "name" +
                                    "}" +
                                "}" +
                            "}" +
                        "\"" +
                "}";

        given()
                .body(requestBody)
                .post("/graphql/")
                .then()
                .contentType(ContentType.JSON)
                .body("data.size()", is(1))
                .body("data.film.title", containsString("Hope"))
                .body("data.film.director", is("George Lucas"))
                .body("data.film.heroes.name", hasItem("Luke"))
                .statusCode(200);
    }

    @Test
    public void createHero() {

        String requestBody =
                "{\"query\":" +
                        "\"" +
                                "mutation addHero { " +
                                       "createHero" +
                                            "(hero: " +
                                                "{"+
                                                    "name: \\\"Han\\\" " +
                                                    "surname: \\\"Solo\\\" " +
                                                    "height: 1.85" +
                                                "}" +
                                            ")" +
                                            "{" +
                                                "name " +
                                                "surname" +
                                            "}" +
                                "}" +
                        "\"" +
                "}";

        given()
                .body(requestBody)
                .contentType(ContentType.JSON)
                .post("/graphql/")
                .then()
                .contentType(ContentType.JSON)
                .body("data.createHero.name", is("Han"))
                .body("data.createHero.surname", is("Solo"))
                .statusCode(200);

    }


    @Test
    public void deleteHero() {

        String requestBody =
                "{\"query\":" +
                        "\"" +
                            "mutation DeleteHero { " +
                                       "deleteHero" +
                                            "(id: 3)" +
                                            "{" +
                                                "name " +
                                                "surname" +
                                            "}" +
                                "}" +
                        "\"" +
                "}";

        given()
                .body(requestBody)
                .contentType(ContentType.JSON)
                .post("/graphql/")
                .then()
                .contentType(ContentType.JSON)
                .body("data.deleteHero.name", is("Han"))
                .body("data.deleteHero.surname", is("Solo"))
                .statusCode(200);

    }

}
