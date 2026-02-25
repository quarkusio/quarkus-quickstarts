package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Movie extends PanacheEntity {

    @NotBlank
    public String title;

    @Min(0) @Max(5)
    public int rating;

}
