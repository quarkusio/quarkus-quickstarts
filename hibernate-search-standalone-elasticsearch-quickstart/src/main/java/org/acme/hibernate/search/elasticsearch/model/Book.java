package org.acme.hibernate.search.elasticsearch.model;

import java.util.UUID;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ProjectionConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.SearchEntity;

@SearchEntity
public class Book {

    @KeywordField
    public UUID id;

    @FullTextField(analyzer = "english")
    public String title;

    @ProjectionConstructor
    public Book(UUID id, String title) {
        this.id = id;
        this.title = title;
    }
}
