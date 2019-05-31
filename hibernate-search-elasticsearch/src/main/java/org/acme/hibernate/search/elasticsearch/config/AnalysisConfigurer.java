package org.acme.hibernate.search.elasticsearch.config;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;
import org.hibernate.search.backend.elasticsearch.analysis.model.dsl.ElasticsearchAnalysisDefinitionContainerContext;

public class AnalysisConfigurer implements ElasticsearchAnalysisConfigurer {

    @Override
    public void configure(ElasticsearchAnalysisDefinitionContainerContext context) {
        context.analyzer("name").custom()
                .withTokenizer("standard")
                .withTokenFilters("asciifolding", "lowercase");

        context.analyzer("english").custom()
                .withTokenizer("standard")
                .withTokenFilters("asciifolding", "lowercase", "porter_stem");

        context.normalizer("sort").custom()
                .withTokenFilters("asciifolding", "lowercase");
    }
}
