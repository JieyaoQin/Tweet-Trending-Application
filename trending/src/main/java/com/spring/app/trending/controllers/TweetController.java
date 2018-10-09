package com.spring.app.trending.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.app.trending.models.Tweet;
import com.spring.app.trending.repositories.KeyPairRepository;
import com.spring.app.trending.repositories.TweetRepository;

import ch.qos.logback.classic.Logger;

@Controller
public class TweetController {
	
	@Autowired
	TweetRepository tweetRepository;
	
	@Autowired
	KeyPairRepository keyPairRepository;
	
	
	@RequestMapping("/home")
	public String home(Model model) {
		model.addAttribute("tweetList", tweetRepository.findAll());
		model.addAttribute("keyPairList", keyPairRepository.findAll());
		return "home";
	}
	
	@RequestMapping(value = "/addTweet", method = RequestMethod.POST)
	public String addTweet(@ModelAttribute Tweet tweet) {
		tweetRepository.save(tweet);
		return "redirect:/home";
	}
	
	
}
