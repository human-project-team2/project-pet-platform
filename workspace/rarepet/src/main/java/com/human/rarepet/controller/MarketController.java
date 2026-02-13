package com.human.rarepet.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class MarketController {
	
	@GetMapping(path={"/market"})
	public String market() {
		
		
				
		return "market/list";
	}
	
	@GetMapping(path="/market/detail")
	public String marketDetail() {
		return "market/detail";
	}
}
