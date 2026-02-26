package com.human.rarepet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/review")
public class ReviewController {

	@GetMapping(path = { "/review" })
	public String review() {
		return "/review/review";  
	}
	
	@GetMapping(path = { "/write" })
	public String write() {
		return "/review/write";  
	}
}
