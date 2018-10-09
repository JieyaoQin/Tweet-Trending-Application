package com.spring.app.trending.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Tweets")
public class Tweet {
	
	@Id
	private String id;
	
	private String message;
	private List<String> keyword;
	private String date;
	
	public Tweet(String message, List<String> keyword, String date) {
		this.message = message;
		this.keyword = keyword;
		this.date = date;
	}
	
	public String getId() {
		return id;
	}
	
	public String getUrl() {
		return message;
	}
	
	public String getTime() {
		return date;
	}
	
	public List<String> getKeywords() {
		return keyword;
	}
	
	public List<String> parseKeywords(String s) {
		List<String> list = new ArrayList<>();
		String[] ss = s.split(",");
		
		for(int i = 0; i < ss.length; i++) {
			if(ss[i] == null && ss[i].trim().length() == 0) continue;
			list.add(ss[i].trim());
		}
		return list;
	}
}
