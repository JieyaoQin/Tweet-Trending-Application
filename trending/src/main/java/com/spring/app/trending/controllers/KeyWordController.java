package com.spring.app.trending.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.app.trending.models.KeyWord;
import com.spring.app.trending.repositories.KeyWordRepository;

@Controller
public class KeyWordController {
	@Autowired
	KeyWordRepository keyWordRepository;
	
	@RequestMapping("/home-key-word")
	public String homeKeyPair(Model model) {
		model.addAttribute("keyWordList", keyWordRepository.findAll());
		return "home-key-word";
	}
	
	@RequestMapping(value = "/add-key-word", method = RequestMethod.POST)
	public String addKeyPair(@ModelAttribute KeyWord keyWord) {
		keyWordRepository.save(keyWord);
		return "redirect:/home-key-word";
	}
}
