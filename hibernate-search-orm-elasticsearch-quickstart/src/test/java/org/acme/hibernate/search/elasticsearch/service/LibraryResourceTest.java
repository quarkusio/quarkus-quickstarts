/*
 * Copyright 2019 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.acme.hibernate.search.elasticsearch.service;

import static org.hamcrest.Matchers.contains;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@QuarkusTest
public class LibraryResourceTest {

    @Test
    public void testLibrary() throws Exception {
        // Full text search
        RestAssured.when().get("/library/author/search?pattern=john").then()
                .statusCode(200)
                .body("firstName", contains("John"),
                        "lastName", contains("Irving"));

        RestAssured.when().get("/library/author/search?pattern=vertigo").then()
                .statusCode(200)
                .body("firstName", contains("Paul"),
                        "lastName", contains("Auster"));

        RestAssured.when().get("/library/author/search?pattern=mystery").then()
                .statusCode(200)
                .body("firstName", contains("John"),
                        "lastName", contains("Irving"));

        // Add an author
        RestAssured.given()
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
                .formParam("firstName", "David")
                .formParam("lastName", "Wrong")
                .put("/library/author/")
                .then()
                .statusCode(204);

        Integer davidLodgeId = RestAssured.when().get("/library/author/search?pattern=dav*").then()
                .statusCode(200)
                .body("firstName", contains("David"),
                        "lastName", contains("Wrong"))
                .extract().path("[0].id");

        // Update an author
        RestAssured.given()
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
                .formParam("firstName", "David")
                .formParam("lastName", "Lodge")
                .post("/library/author/" + davidLodgeId)
                .then()
                .statusCode(204);

        RestAssured.when().get("/library/author/search?pattern=dav*").then()
                .statusCode(200)
                .body("firstName", contains("David"),
                        "lastName", contains("Lodge"));

        // Add a book
        RestAssured.given()
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
                .formParam("title", "Therapy")
                .formParam("authorId", davidLodgeId)
                .put("/library/book/")
                .then()
                .statusCode(204);

        RestAssured.when().get("/library/author/search?pattern=therapy").then()
                .statusCode(200)
                .body("firstName", contains("David"),
                        "lastName", contains("Lodge"));
    }
}
