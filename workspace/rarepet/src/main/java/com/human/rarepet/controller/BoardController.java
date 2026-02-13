package com.human.rarepet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BoardController {
	
	@GetMapping (path = {"/board/boardlist"}) 
	public String list(Model model) {
		
		System.out.println("--------------------Board");
			
		return "board/boardlist"; 
		
	}
	
	@GetMapping (path = {"/board/community"}) 
	public String boardlist(Model model) {
		
		System.out.println("--------------------Board");
			
		return "board/community"; 
		
	}
	/*
	 * @GetMapping(path={"/market"}) public String market() { return
	 * "market/marketlist"; }
	 * 
	 * @GetMapping(path="/market/marketdetail") public String marketDetail() {
	 * return "market/marketdetail"; }
	 */
}
