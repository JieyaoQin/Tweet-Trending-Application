package com.spring.app.trending.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.app.trending.models.CustomModel;
import com.spring.app.trending.models.PostTopKey;

@Controller
public class DualListController {
	
	String topKeyword = null;
	CustomModel m = new CustomModel();

	@RequestMapping(value="/lists", method=RequestMethod.GET)
	public String index_lists(PostTopKey post, Model model) {
		// top 10
		if(topKeyword == null) {
			List<String> kws = m.getTopTen();
			topKeyword = kws.get(0);
			model.addAttribute("keywords", kws);
		}
		// query related keywords
		else {
			List<String> kws = m.getKeyPair(topKeyword);
			
			// if list is empty, get top ten instead
			if(kws == null || kws.isEmpty()) {
				kws = m.getTopTen();
				topKeyword = kws.get(0);
			}
			model.addAttribute("keywords", (Iterable<String>)kws);
		}
		
		List<String> urls = m.getURL(topKeyword);
		model.addAttribute("urls", (Iterable<String>)urls);
		
		model.addAttribute("top", topKeyword);
		
		return "lists";
	}
	
	@RequestMapping(value = "/keywordSearch", method = RequestMethod.POST)
	public String search(@Valid PostTopKey post, Model model) {
		model.addAttribute("key", post.getKey());
		topKeyword = post.getKey();
		return "redirect:/lists";
	}
	
	@RequestMapping(value="lists/keyword/*", method = RequestMethod.GET)
	public String getKeyword(@RequestParam("keyword") String keyword){

	    topKeyword = keyword;
	    return "redirect:/lists";
	}
	
}
