
/*
 * Copyright 2019 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class JsonEndpointTest {

    @Test
    public void testJsonObject() {
        given()
                .when().get("/hello/Tim/object")
                .then()
                .statusCode(200)
                .body("Hello", is(equalTo("Tim")));
    }

    @Test
    public void testJsonArray() {
        given()
                .when().get("/hello/Tim/array")
                .then()
                .statusCode(200)
                .body("", is(equalTo(Arrays.asList("Hello", "Tim"))));
    }

}
