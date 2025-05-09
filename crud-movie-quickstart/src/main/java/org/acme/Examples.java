package org.acme;

final class Examples {
    private Examples() {

    }

    static final String VALID_EXAMPLE_MOVIE = """
            {
              "id": 1,
              "name": "Inception",
              "rating": 5
            }
            """;

    static final String VALID_EXAMPLE_MOVIE_TO_CREATE = """
               {
                 "name": "Inception",
                 "rating": 4
               }
            """;

    static final String VALID_EXAMPLE_MOVIE_LIST = "[" + VALID_EXAMPLE_MOVIE + "]";
}