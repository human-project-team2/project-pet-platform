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
		
		System.out.println("--------------------Community");
			
		return "board/community"; 
		
	}
	
	@GetMapping (path = {"/board/qna"}) 
	public String boardqna(Model model) {
		
		System.out.println("--------------------QnA");
			
		return "board/qna"; 
		
	}


}
