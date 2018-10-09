package com.spring.app.trending.models;

public class Post {
	String title;
	Integer count;
	
	public Post(String title, Integer count) {
		this.title = title;
		this.count = count;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Integer getCount() {
		return count;
	}
}
