package com.spring.app.trending.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.app.trending.models.KeyPair;
import com.spring.app.trending.models.Tweet;
import com.spring.app.trending.repositories.KeyPairRepository;
import com.spring.app.trending.repositories.TweetRepository;

@Controller
public class KeyPairController {
	@Autowired
	KeyPairRepository keyPairRepository;
	
	@RequestMapping("/home-key-pair")
	public String homeKeyPair(Model model) {
		model.addAttribute("keyPairList", keyPairRepository.findAll());
		return "home-key-pair";
	}
	
	@RequestMapping(value = "/add-key-pair", method = RequestMethod.POST)
	public String addKeyPair(@ModelAttribute KeyPair keyPair) {
		keyPairRepository.save(keyPair);
		return "redirect:/home-key-pair";
	}
}
