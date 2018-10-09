package com.spring.app.trending.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.spring.app.trending.models.KeyPair;

public interface KeyPairRepository extends CrudRepository<KeyPair, String> {
	
}
