package com.human.rarepet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.human.rarepet.dto.ProductRegisterDto;

@Controller
@RequestMapping("/auth")
public class AuthController {

	@GetMapping(path = { "/login" })
	public String login() {

		return "/auth/login";
	}

	@GetMapping(path = { "/register" })
	public String register() {
		return "/auth/register";
	}

	@PostMapping("/login")
	public String loginProcess() {

		return "/home";
	}
}