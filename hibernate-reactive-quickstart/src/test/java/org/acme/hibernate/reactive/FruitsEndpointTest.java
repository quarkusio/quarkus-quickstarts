package org.acme.hibernate.reactive;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.IsEmptyString.emptyString;

@QuarkusTest
public class FruitsEndpointTest {

	private static final String BASE_URL = "/fruits";

    @Test
    void testListAllFruits() {
        //List all, should have all 3 fruits the database has initially:
		Response response = given()
				.when()
				.get(BASE_URL)
				.then()
				.statusCode(200)
				.contentType(APPLICATION_JSON)
				.extract().response();

		assertThat(response.jsonPath().getList("name"))
				.containsExactlyInAnyOrder("Cherry", "Apple", "Banana");

        // Update Cherry to Pineapple
        given()
			.when()
				.body("{\"name\" : \"Pineapple\"}")
				.contentType(APPLICATION_JSON)
				.put(BASE_URL + "/1")
			.then()
				.statusCode(200)
				.body(
					containsString("\"id\":"),
					containsString("\"name\":\"Pineapple\""));

        //List all, Pineapple should've replaced Cherry:
		response = given()
			.when()
				.get(BASE_URL)
			.then()
				.statusCode(200)
				.contentType(APPLICATION_JSON)
				.extract().response();
		assertThat(response.jsonPath().getList("name")).containsExactlyInAnyOrder("Pineapple", "Apple", "Banana");

        //Delete Pineapple:
        given()
			.when()
				.delete(BASE_URL+ "/1")
			.then()
				.statusCode(204);

        //List all, Pineapple should be missing now:
        given()
			.when()
				.get(BASE_URL)
			.then()
                .statusCode(200)
                .body(
					not(containsString( "Pineapple")),
					containsString("Apple"),
					containsString("Banana"));

        //Create the Pear:
        given()
			.when()
				.body("{\"name\" : \"Pear\"}")
				.contentType(APPLICATION_JSON)
				.post(BASE_URL)
			.then()
				.statusCode(201)
				.body(
					containsString("\"id\":"),
					containsString("\"name\":\"Pear\""));

        //List all, Pineapple should be still missing now:
        response = given()
			.when()
				.get(BASE_URL)
			.then()
				.statusCode(200)
				.extract().response();
		assertThat(response.jsonPath().getList("name")).containsExactlyInAnyOrder("Pear", "Apple", "Banana");
    }

    @Test
    void testEntityNotFoundForDelete() {
        given()
			.when()
				.delete(BASE_URL+ "/9236")
			.then()
				.statusCode(404)
				.body(emptyString());
    }

    @Test
    void testEntityNotFoundForUpdate() {
        given()
			.when()
				.body("{\"name\" : \"Watermelon\"}")
				.contentType(APPLICATION_JSON)
				.put(BASE_URL + "/32432")
			.then()
				.statusCode(404)
				.body(emptyString());
    }
}
