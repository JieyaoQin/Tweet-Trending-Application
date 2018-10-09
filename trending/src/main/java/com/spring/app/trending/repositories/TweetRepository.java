package com.spring.app.trending.repositories;

import java.util.Map;

import org.springframework.data.repository.CrudRepository;

import com.spring.app.trending.models.Tweet;

public interface TweetRepository extends CrudRepository<Tweet, String> {
}
