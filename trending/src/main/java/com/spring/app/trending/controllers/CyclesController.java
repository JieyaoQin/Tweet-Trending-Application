package com.spring.app.trending.controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.app.trending.models.CustomModel;
import com.spring.app.trending.models.KeyWord;
import com.spring.app.trending.models.PostTopKey;

@Controller
public class CyclesController {
	
	String topKeyword = null;
	CustomModel m = new CustomModel();

	@RequestMapping(value="/cycles", method=RequestMethod.GET)
	public String index_lists(PostTopKey post, Model model) {
		Timestamp ts0 = new Timestamp(System.currentTimeMillis());
		System.out.println("STARTING... " + (ts0));
		
		// top 10
		if(topKeyword == null) {
			List<KeyWord> pairs = m.getTopTenFreq();
//			topKeyword = pairs.get(0).getKeyword();
			Timestamp ts1 = new Timestamp(System.currentTimeMillis());
			System.out.println("TIME OF GETTING TOP TEN FROM CUSTOM MODEL: " + ((ts1.getTime() - ts0.getTime())));
			
			List<String> kws = new ArrayList<>();
			List<Integer> freqs = new ArrayList<>();
			for(KeyWord kw: pairs) {
				kws.add(kw.getKeyword());
				freqs.add(kw.getFrequency());
			}
			model.addAttribute("keywords", kws);
			model.addAttribute("frequencies", freqs);
			Timestamp ts2 = new Timestamp(System.currentTimeMillis());
			System.out.println("TIME OF ADDING TOP TEN TO MODEL: " + ((ts2.getTime() - ts1.getTime())));
			
			List<String> urls = m.getURL(pairs.get(0).getKeyword());
			model.addAttribute("urls", (Iterable<String>)urls);
			Timestamp ts3 = new Timestamp(System.currentTimeMillis());
			System.out.println("TIME OF ADDING URL IN TOP TEN: " + ((ts3.getTime() - ts2.getTime())));
			
		}
		// query related keywords
		else {
			// System.out.println("QUERY TARGET: " + topKeyword);
			List<KeyWord> pairs = m.getKeyPairFreq(topKeyword);
			// System.out.println("RECEIVED PAIRS: " + pairs);
			Timestamp ts4 = new Timestamp(System.currentTimeMillis());
			System.out.println("TIME OF GETTING KEYPAIRS FROM CUSTOM MODEL: " + ((ts4.getTime() - ts0.getTime())));
			
			// if list is empty, get top ten instead
			if(pairs == null || pairs.isEmpty()) {
				return "redirect:/cycles/reset";
			}
			
			
			List<String> kws = new ArrayList<>();
			List<Integer> freqs = new ArrayList<>();
			for(KeyWord kw: pairs) {
				kws.add(kw.getKeyword());
				freqs.add(kw.getFrequency());
			}
			model.addAttribute("keywords", (Iterable<String>)kws);
			model.addAttribute("frequencies", freqs);
			Timestamp ts5 = new Timestamp(System.currentTimeMillis());
			System.out.println("TIME OF ADDING KEYPAIRS TO MODEL: " + ((ts5.getTime() - ts4.getTime())));
			
			List<String> urls = m.getURL(topKeyword);
			model.addAttribute("urls", (Iterable<String>)urls);
			Timestamp ts6 = new Timestamp(System.currentTimeMillis());
			System.out.println("TIME OF ADDING URL IN KEYPAIRS: " + ((ts6.getTime() - ts5.getTime())));
			
		}
		
//		List<String> urls = m.getURL(topKeyword);
		
		StringBuilder str = new StringBuilder();
		if(topKeyword == null) str.append("Select Your Keyword :)");
		else str.append("Current Keyword: ").append(topKeyword);
		model.addAttribute("top", str.toString());
		
		Timestamp ts7 = new Timestamp(System.currentTimeMillis());
		System.out.println("TOTAL TIME: " + ((ts7.getTime() - ts0.getTime())));
		
		return "bubble";
	}
	
	@RequestMapping(value = "/cycles/search", method = RequestMethod.POST)
	public String search(PostTopKey post, Model model) {
		// System.out.println("RECEIVED OBJECT: " + post);
		// System.out.println("RECEIVED OBJECT MESSAGE: " + post.getKey());
		try {
			// model.addAttribute("key", post.getKey());
			if(post == null 
					|| post.getKey() == null 
					|| post.getKey().trim().length() == 0) {
				return "redirect:/cycles/reset";
			}
			topKeyword = post.getKey();
			return "redirect:/cycles";
		} catch(Exception e) {
			return "redirect:/cycles/reset";
		}
		
	}
	
	@RequestMapping(value="/cycles/keyword/*", method = RequestMethod.GET)
	public String getKeyword(@RequestParam("keyword") String keyword){

	    topKeyword = keyword;
	    return "redirect:/cycles";
	}
	
	@RequestMapping(value = "/cycles/reset", method = RequestMethod.GET)
	public String reset(Model model) {
		// reset to original page
		topKeyword = null;
		return "redirect:/cycles";
	}
	
}
