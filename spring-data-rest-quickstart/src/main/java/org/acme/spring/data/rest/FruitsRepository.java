package org.acme.spring.data.rest;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface FruitsRepository extends PagingAndSortingRepository<Fruit, Long> {
}
