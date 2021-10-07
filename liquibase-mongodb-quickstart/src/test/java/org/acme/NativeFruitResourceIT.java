package org.acme;

import io.quarkus.test.junit.NativeImageTest;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.util.List;

import static io.restassured.RestAssured.get;

@NativeImageTest
@DisabledOnOs(OS.WINDOWS)
class NativeFruitResourceIT {
    @Test
    public void testTheEndpoint() {
        // assert that a fruit exist as one has been created in the changelog
        List<Fruit> list = get("/fruits").as(new TypeRef<List<Fruit>>() {
        });
        Assertions.assertEquals(1, list.size());
    }
}
