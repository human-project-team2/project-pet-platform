package com.human.rarepet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	@GetMapping(path = {"/login"})
	public String login() {
		
		return "/auth/login";
	}
	
	@GetMapping(path = {"/register"})
	public String register() {	
		return "/auth/register"; // /resources/templates/ + register + .html
	}
}
