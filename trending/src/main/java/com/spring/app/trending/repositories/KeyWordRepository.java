package com.spring.app.trending.repositories;

import java.util.Map;

import org.springframework.data.repository.CrudRepository;

import com.spring.app.trending.models.KeyWord;

public interface KeyWordRepository extends CrudRepository<KeyWord, String> {
	
}
