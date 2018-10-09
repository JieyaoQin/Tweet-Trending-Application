package com.spring.app.trending.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.app.trending.models.CustomModel;
import com.spring.app.trending.models.KeyWord;
import com.spring.app.trending.repositories.KeyPairRepository;
import com.spring.app.trending.repositories.TweetRepository;

@Controller
public class TestController_BU {
	
	String topKeyword;
	CustomModel m = new CustomModel();

//	
////	@Autowired
////	TweetRepository tweetRepository;
////	
////	@Autowired
////	KeyPairRepository keyPairRepository;
//	
//	
	@RequestMapping("/test")
	public String home(Model model) {
		// homepage: show top 10 based on frequency
		if(topKeyword == null) {
			List<String> kws = m.getTopTen();
			topKeyword = kws.get(0);
			model.addAttribute("keywords", kws);
		}
		// query related keywords
		else {
			List<String> kws = m.getKeyPair(topKeyword);
			model.addAttribute("keywords", (Iterable<String>)kws);
		}
		
		List<String> urls = m.getURL(topKeyword);
		model.addAttribute("urls", (Iterable<String>)urls);
		
		model.addAttribute("top", topKeyword);
		
		return "test";
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String addTweet(@PathVariable String key) {
		topKeyword = key;
		return "redirect:/test";
	}
	
	
}
