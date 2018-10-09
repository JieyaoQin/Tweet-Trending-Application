package com.spring.app.trending;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// spring.data.mongodb.database=TwitterStream
// spring.data.mongodb.host=localhost
// spring.data.mongodb.port=27017

@SpringBootApplication
public class TrendingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrendingApplication.class, args);
	}
}
