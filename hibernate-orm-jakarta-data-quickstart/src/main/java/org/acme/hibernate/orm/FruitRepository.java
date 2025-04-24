package org.acme.hibernate.orm;

import java.util.stream.Stream;

import jakarta.data.Order;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Delete;
import jakarta.data.repository.Find;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

@Repository
public interface FruitRepository extends CrudRepository<Fruit, Integer> {

	@Find
	Stream<Fruit> findAll(Order<Fruit> order);

	@Query("update Fruit f set f.name = :name where f.id = :id")
	void update(Integer id, String name);

	@Delete
	void delete(Integer id);

}
