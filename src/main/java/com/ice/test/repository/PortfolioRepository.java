package com.ice.test.repository;

import java.util.Collection;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ice.test.model.Portfolio;

@Repository
public interface PortfolioRepository extends CrudRepository<Portfolio, Long>{
	
	Collection<Portfolio> findAll();
}
