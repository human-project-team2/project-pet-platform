package com.human.rarepet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = {"/board"})
public class BoardController {
	
	@GetMapping (path = {"/boardlist"}) 
	public String list(Model model) {
		
		System.out.println("--------------------Board");
			
		return "board/boardlist"; 
		
	}
}
