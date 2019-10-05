package com.week5.controller;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.week5.service.CrawlingService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Inject
	CrawlingService crawlingService;

	@RequestMapping(value = "/crawling", method = RequestMethod.GET)
	public String home(Locale locale, Model model) throws Exception {
		
		crawlingService.insightCrawling("/section/entertainment?", "entertainment");
		
		crawlingService.huffingtonPostCrawling("news/society/");
		
		return "home";
	}
	
}
