package org.acme.hibernate.orm;

import javax.persistence.*;
import java.util.Set;

@javax.persistence.Entity
@Table(name = "known_fruits")

@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Fruit.graph.detail",
                attributeNodes =
                        {
                                @NamedAttributeNode(value = "fruitLocations")
                        }),
})

@NamedQuery(name = "Fruits.findAll", query = "SELECT f FROM Fruit f ORDER BY f.name", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@Cacheable
public class Fruit {

    @Id
    @SequenceGenerator(name = "fruitsSequence", sequenceName = "known_fruits_id_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "fruitsSequence")
    private Integer id;

    @Column(length = 40, unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "idFruit")
    private Set<FruitLocation> fruitLocations;

    public Fruit() {
    }

    public Fruit(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<FruitLocation> getFruitLocations() {
        return fruitLocations;
    }

    public void setFruitLocations(Set<FruitLocation> fruitLocations) {
        this.fruitLocations = fruitLocations;
    }
}
