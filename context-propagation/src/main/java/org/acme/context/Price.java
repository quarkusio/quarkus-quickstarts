package org.acme.context;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Price extends PanacheEntity  {
    public Double value;
}
