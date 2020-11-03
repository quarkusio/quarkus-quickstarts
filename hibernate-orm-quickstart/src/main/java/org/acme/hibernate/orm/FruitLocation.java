package org.acme.hibernate.orm;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "FruitLocation")
@NamedQuery(name = "FruitLocation.findAll", query = "SELECT f FROM FruitLocation f ORDER BY f.location", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@Cacheable
public class FruitLocation {

    @Id
    @SequenceGenerator(name = "fruitLocationSequence", sequenceName = "FruitLocation_id_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "fruitLocationSequence")
    private Integer id;

    @Column(length = 40, unique = true)
    private String location;

    //    @Basic(optional = false)
    @NotNull
    @Column(name = "idFruit", nullable = false)
//    @Size(min = 1, max = 36)
//    @JsonView(Views.Abridged.class)
//    @Schema(description = "ID of the beam", required = true)
    private Integer idFruit; // Foreign Key

    public FruitLocation() {
    }

    public FruitLocation(String location) {
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getIdFruit() {
        return idFruit;
    }

    public void setIdFruit(Integer idFruit) {
        this.idFruit = idFruit;
    }
}
