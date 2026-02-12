package com.human.rarepet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeContoroller {

	@GetMapping(path = {"/","/home"})
	public String home() {
		
		return "index"; // /resources/templates/ + index + .html
	}
}
