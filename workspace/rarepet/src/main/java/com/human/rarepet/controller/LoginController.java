package com.human.rarepet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class LoginController {
	
	@GetMapping(path = {"/login"})
	public String login() {
		
		return "/login/login";
	}
	
	@GetMapping(path = {"/register"})
	public String register() {	
		return "/login/register"; // /resources/templates/ + register + .html
	}
}
