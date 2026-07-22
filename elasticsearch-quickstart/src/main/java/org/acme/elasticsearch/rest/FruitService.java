package org.acme.elasticsearch.rest;

import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.elasticsearch.Fruit;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import co.elastic.clients.transport.rest5_client.low_level.Request;
import co.elastic.clients.transport.rest5_client.low_level.Response;
import co.elastic.clients.transport.rest5_client.low_level.Rest5Client;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class FruitService {
    @Inject
    Rest5Client restClient;

    public void index(Fruit fruit) throws Exception {
        Request request = new Request(
            "PUT",
            "/fruits/_doc/" + fruit.id);
        request.setJsonEntity(JsonObject.mapFrom(fruit).toString());
        restClient.performRequest(request);
    }

    public Fruit get(String id) throws Exception {
        Request request = new Request(
            "GET",
            "/fruits/_doc/" + id);
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject json = new JsonObject(responseBody);
        return json.getJsonObject("_source").mapTo(Fruit.class);
    }

    public List<Fruit> searchByColor(String color) throws Exception {
        return search("color", color);
    }

    public List<Fruit> searchByName(String name) throws Exception {
        return search("name", name);
    }

    private List<Fruit> search(String term, String match) throws Exception {
        Request request = new Request(
            "GET",
            "/fruits/_search");
        //construct a JSON query like {"query": {"match": {"<term>": "<match"}}
        JsonObject termJson = new JsonObject().put(term, match);
        JsonObject matchJson = new JsonObject().put("match", termJson);
        JsonObject queryJson = new JsonObject().put("query", matchJson);
        request.setJsonEntity(queryJson.encode());
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());

        JsonObject json = new JsonObject(responseBody);
        JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
        List<Fruit> results = new ArrayList<>(hits.size());
        for (int i = 0; i < hits.size(); i++) {
            JsonObject hit = hits.getJsonObject(i);
            Fruit fruit = hit.getJsonObject("_source").mapTo(Fruit.class);
            results.add(fruit);
        }
        return results;
    }
}
