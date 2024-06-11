package org.acme.spring.data.rest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FruitsRepository extends JpaRepository<Fruit, Long> {
}
