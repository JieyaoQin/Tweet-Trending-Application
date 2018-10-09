package com.spring.app.trending.models;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "KeyPair")
public class KeyPair {
	@Id
	private String id;
	
	private String keyword;
	private Map<String, Long> children;
	
	public KeyPair(String keyword, Map<String, Long> children) {
		this.keyword = keyword;
		this.children = children;
	}
	
	public String getId() {
		return id;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public Map<String, Long> getChildren() {
		return children;
	}
}
