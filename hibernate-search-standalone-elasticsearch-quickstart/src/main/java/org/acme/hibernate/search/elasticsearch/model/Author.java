package org.acme.hibernate.search.elasticsearch.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.search.engine.backend.types.Sortable;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IdProjection;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.ProjectionConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.SearchEntity;

@SearchEntity
@Indexed
public class Author {

    @DocumentId
    public UUID id;

    @FullTextField(analyzer = "name")
    @KeywordField(name = "firstName_sort", sortable = Sortable.YES, normalizer = "sort")
    public String firstName;

    @FullTextField(analyzer = "name")
    @KeywordField(name = "lastName_sort", sortable = Sortable.YES, normalizer = "sort")
    public String lastName;

    @IndexedEmbedded
    public List<Book> books = new ArrayList<>();

    @ProjectionConstructor
    public Author(@IdProjection UUID id, String firstName, String lastName, List<Book> books) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.books = books;
    }
}
