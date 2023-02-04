package org.acme.extra;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@Path("/hello")
public class VertxJsonResource {

    @GET
    @Path("{name}/object")
    public JsonObject jsonObject(String name) {
        return new JsonObject().put("Hello", name);
    }

    @GET
    @Path("{name}/array")
    public JsonArray jsonArray(String name) {
        return new JsonArray().add("Hello").add(name);
    }
}
