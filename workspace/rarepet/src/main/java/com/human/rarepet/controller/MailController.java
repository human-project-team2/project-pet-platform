package com.human.rarepet.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.human.rarepet.service.AuthService;

import jakarta.servlet.http.HttpSession;


@Controller
public class MailController {
	
	public final AuthService authService;
	
	public MailController(AuthService authService) {
		this.authService = authService;
	}
	
	@GetMapping(path = {"/send-email"})
	@ResponseBody
	public String sendMail(@RequestParam("email") String email, HttpSession session) {
		
		boolean alreadyUsedEmail = authService.checkDuplicateEmail(email);
		
		if(alreadyUsedEmail) {
			return "alreadyUsedEmail";
		}
		
		int mailCode = authService.makeCode();
		session.setAttribute("mailCode", mailCode);
		boolean codeSent = authService.sendEmail(email, mailCode);
		
		if(codeSent) {
			return "success";
		} else {
			return "failure";
		}
	}
	
	@PostMapping(path = {"/send-email-code"})
	@ResponseBody
	public String confirmMail(@RequestParam("code") String code, HttpSession session) {
		
		Integer mailCode = (Integer)session.getAttribute("mailCode");
		
		if(mailCode != null && code.equals(mailCode.toString())) {
			return "success";
		} else {
			return "failure";
		}
		
	}
}
