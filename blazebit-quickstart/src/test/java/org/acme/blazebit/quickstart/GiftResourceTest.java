package org.acme.blazebit.quickstart;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;

@QuarkusTest
@TestHTTPEndpoint(GiftResource.class)
public class GiftResourceTest {

	@Test
	void testCreateGift() throws Exception {
		Response response = given().contentType(ContentType.JSON).body(new JsonObject(Map.of("name", "From Christian to Christian")).toString()).post().andReturn();
		response.then().log().body().statusCode(201);
	}

	@Test
	void testGetGifts() {
		Response response = given().get().andReturn();
		response.then().statusCode(200).assertThat().body("size()", Matchers.greaterThanOrEqualTo(2));
	}

	@Test
	void testUpdateGifts() {
		Response response = given().contentType(ContentType.JSON).body(new JsonObject(Map.of("name", "This had to be changed")).toString()).put("2").andReturn();
		response.then().statusCode(200).assertThat().body("name", is("This had to be changed"));
	}

	@Test
	void testGetGift() throws Exception {
		Response response = given().get("1").andReturn();
		response.then().statusCode(200).assertThat().body("name", is("Lego 10277"));
	}
}