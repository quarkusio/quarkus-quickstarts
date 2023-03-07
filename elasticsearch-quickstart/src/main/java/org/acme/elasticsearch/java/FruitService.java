package org.acme.elasticsearch.java;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.GetRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.elasticsearch.Fruit;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class FruitService {
    @Inject
    ElasticsearchClient client;

    public void index(Fruit fruit) throws IOException {
        IndexRequest<Fruit> request = IndexRequest.of(
            b -> b.index("fruits")
                .id(fruit.id)
                .document(fruit));
        client.index(request);
    }

    public Fruit get(String id) throws IOException {
        GetRequest getRequest = GetRequest.of(
            b -> b.index("fruits")
                .id(id));
        GetResponse<Fruit> getResponse = client.get(getRequest, Fruit.class);
        if (getResponse.found()) {
            return getResponse.source();
        }
        return null;
    }

    public List<Fruit> searchByColor(String color) throws IOException {
        return search("color", color);
    }

    public List<Fruit> searchByName(String name) throws IOException {
        return search("name", name);
    }

    private List<Fruit> search(String term, String match) throws IOException {
        SearchRequest searchRequest = SearchRequest.of(
            b -> b.index("fruits")
                .query(QueryBuilders.match().field(term).query(FieldValue.of(match)).build()._toQuery()));

        SearchResponse<Fruit> searchResponse = client.search(searchRequest, Fruit.class);
        HitsMetadata<Fruit> hits = searchResponse.hits();
        return hits.hits().stream().map(hit -> hit.source()).collect(Collectors.toList());
    }
}
