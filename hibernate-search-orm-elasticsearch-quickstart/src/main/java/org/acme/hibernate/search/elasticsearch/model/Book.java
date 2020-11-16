package org.acme.hibernate.search.elasticsearch.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Indexed
public class Book extends PanacheEntity {

    @FullTextField(analyzer = "english")
    public String title;

    @ManyToOne
    @JsonIgnore
    public Author author;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }

        Book other = (Book) o;

        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
