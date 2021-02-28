package org.acme.hibernate.reactive;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "known_fruits")
@NamedQuery(name = Fruit.FIND_ALL, query = "SELECT f FROM Fruit f ORDER BY f.name")
public class Fruit {
    public static final String FIND_ALL = "Fruits.findAll";

    @Id
    @SequenceGenerator(name = "fruitsSequence", sequenceName = "known_fruits_id_seq",
            allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "fruitsSequence")
    private Integer id;

    @Column(length = 40, unique = true)
    private String name;

    public Fruit(String name) {
        this.name = name;
    }
}
