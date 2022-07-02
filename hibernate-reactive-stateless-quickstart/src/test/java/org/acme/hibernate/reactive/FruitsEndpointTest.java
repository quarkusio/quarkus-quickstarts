package org.acme.hibernate.reactive;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.text.IsEmptyString.emptyString;

@QuarkusTest
public class FruitsEndpointTest {

    @Test
    public void testListAllFruits() {
        //List all, should have all 3 fruits the database has initially:
		Response response = given()
				.when()
				.get("/fruits")
				.then()
				.statusCode(200)
				.contentType("application/json")
				.extract().response();
		assertThat(response.jsonPath().getList("name")).containsExactlyInAnyOrder("Cherry", "Apple", "Banana");

        // Update Cherry to Pineapple
        given()
			.when()
				.body("{\"name\" : \"Pineapple\"}")
				.contentType("application/json")
				.put("/fruits/1")
			.then()
				.statusCode(200)
				.body(
					containsString("\"id\":"),
					containsString("\"name\":\"Pineapple\""));

        //List all, Pineapple should've replaced Cherry:
		response = given()
			.when()
				.get("/fruits")
			.then()
				.statusCode(200)
				.contentType("application/json")
				.extract().response();
		assertThat(response.jsonPath().getList("name")).containsExactlyInAnyOrder("Pineapple", "Apple", "Banana");

        //Delete Pineapple:
        given()
			.when()
				.delete("/fruits/1")
			.then()
				.statusCode(204);

        //List all, Pineapple should be missing now:
        given()
			.when()
				.get("/fruits")
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
				.contentType("application/json")
				.post("/fruits")
			.then()
				.statusCode(201)
				.body(
					containsString("\"id\":"),
					containsString("\"name\":\"Pear\""));

        //List all, Pineapple should be still missing now:
        response = given()
			.when()
				.get("/fruits")
			.then()
				.statusCode(200)
				.extract().response();
		assertThat(response.jsonPath().getList("name")).containsExactlyInAnyOrder("Pear", "Apple", "Banana");
    }

    @Test
    public void testEntityNotFoundForDelete() {
        given()
			.when()
				.delete("/fruits/9236")
			.then()
				.statusCode(404)
				.body(emptyString());
    }

    @Test
    public void testEntityNotFoundForUpdate() {
        given()
			.when()
				.body("{\"name\" : \"Watermelon\"}")
				.contentType("application/json")
				.put("/fruits/32432")
			.then()
				.statusCode(404)
				.body(emptyString());
    }
}
