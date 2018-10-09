package com.spring.app.trending.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.app.trending.models.KeyWord;
import com.spring.app.trending.models.Tweet;
import com.spring.app.trending.repositories.KeyPairRepository;
import com.spring.app.trending.repositories.KeyWordRepository;
import com.spring.app.trending.repositories.TweetRepository;

import ch.qos.logback.classic.Logger;

@Controller
public class RankController {
	
	@Autowired
	TweetRepository tweetRepository;
	
	@Autowired
	KeyPairRepository keyPairRepository;
	
	@Autowired
	KeyWordRepository keyWordRepository;
	
	
	@RequestMapping("/home-ranking")
	public String home(Model model) {
//		String value = "Testing...";
//		model.addAttribute("testValue", value);
		
		// get top 10 keywords
		List<KeyWord> keyWordList = (List<KeyWord>) keyWordRepository.findAll();
		Collections.sort(keyWordList, new Comparator<KeyWord>() {
			@Override
			public int compare(KeyWord o1, KeyWord o2) {
				return (int)(o2.getFrequency() - o1.getFrequency());
			}
		});
		List<KeyWord> top10 = new ArrayList<>(keyWordList.subList(0, 10));
		
		// replace related ids with urls
		List<Tweet> tweets = (List<Tweet>) tweetRepository.findAll();
		Map<String, String> id_url = new HashMap<>();
		for(Tweet tweet: tweets) {
			id_url.put(tweet.getId(), tweet.getUrl());
		}
		
		for(KeyWord keyword: top10) {
			List<String> ids = keyword.getMessage();
			for(int i = 0; i < ids.size(); i++) {
				ids.set(i, id_url.get(ids.get(i)));
			}
		}
		
		model.addAttribute("ranking", top10);
		return "home-ranking";
	}
	
	
}
