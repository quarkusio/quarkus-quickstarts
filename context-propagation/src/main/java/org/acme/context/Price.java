package org.acme.context;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Price extends PanacheEntity  {
    public Double value;
}
