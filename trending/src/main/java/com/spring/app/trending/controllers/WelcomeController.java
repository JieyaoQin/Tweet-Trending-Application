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
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.app.trending.models.CustomModel;
import com.spring.app.trending.models.KeyWord;
import com.spring.app.trending.models.Post;
import com.spring.app.trending.models.PostTopKey;
import com.spring.app.trending.repositories.KeyPairRepository;
import com.spring.app.trending.repositories.TweetRepository;

@Controller
public class WelcomeController {
	
	String topKeyword = null;
	CustomModel m = new CustomModel();
	
	@RequestMapping(value="/welcome", method=RequestMethod.GET)
	public String index(Post postO, Model model) {
		return "welcome";
	}
 
	@RequestMapping(value = "/welcomepost", method = RequestMethod.POST)
	public String addNewPost(@Valid Post postO, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "welcome";
		}
		model.addAttribute("title", postO.getTitle());
		model.addAttribute("count", postO.getCount());
		return "welcome";
	}
	
	@RequestMapping(value="/newtest", method=RequestMethod.GET)
	public String index_list(PostTopKey post, Model model) {
		// top 10
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
		
		return "newtest";
	}
	
	@RequestMapping(value = "/newtestsearch", method = RequestMethod.POST)
	public String search(@Valid PostTopKey post, Model model) {
		model.addAttribute("key", post.getKey());
		topKeyword = post.getKey();
		return "redirect:/newtest";
	}
	
	@RequestMapping(value="/keyword/*", method = RequestMethod.GET)
	public String getitem(@RequestParam("keyword") String keyword){

	    topKeyword = keyword;
	    return "redirect:/newtest";
	}
	
}
