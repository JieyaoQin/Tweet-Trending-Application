package com.spring.app.trending.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.app.trending.models.CustomModel;
import com.spring.app.trending.models.KeyWord;
import com.spring.app.trending.models.PostTopKey;
import com.spring.app.trending.repositories.KeyPairRepository;
import com.spring.app.trending.repositories.TweetRepository;

@Controller
public class TestController {
	
//	// inject via application.properties
//	@Value("${welcome.message:test}")
//	private String message = "Hello World";
	
	String topKeyword = null;
	CustomModel m = new CustomModel();

	@RequestMapping("/")
	public String welcome(PostTopKey post, Model model) {
//		model.put("message", this.topKeyword);
//		return "welcome";
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
	
//	@RequestMapping(value = "/search", method = RequestMethod.POST)
//	public String search(@ModelAttribute String key) {
//		topKeyword = key;
//		return "redirect:/test";
//	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String addNewPost(@Valid PostTopKey post, BindingResult bindingResult, Model model) {
		topKeyword = post.getKey();
		return "test";
	}
	
}
