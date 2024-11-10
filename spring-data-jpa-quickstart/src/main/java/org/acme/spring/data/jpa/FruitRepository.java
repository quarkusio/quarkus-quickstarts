package org.acme.spring.data.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FruitRepository extends CrudRepository<Fruit, Long> {

    List<Fruit> findByColor(String color);
}
