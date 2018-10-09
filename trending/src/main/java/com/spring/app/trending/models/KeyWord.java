package com.spring.app.trending.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "KeyWord")
public class KeyWord {
	@Id
	private String id;
	
	private String keyword;
	private List<String> message;
	private Integer frequency;
	
	public KeyWord(String keyword, List<String> message, Integer frequency) {
		this.keyword = keyword;
		this.message = message;
		this.frequency = frequency;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public List<String> getMessage() {
		return message;
	}
	
	public Integer getFrequency() {
		return frequency;
	}
}
