package com.human.rarepet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.ProductRegisterDto;
import com.human.rarepet.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping(path = { "/login" })
	public String login() {

		return "/auth/login";
	}
	
    @PostMapping(path = {"/login"})
    public String loginProcess(@RequestParam("memberId") String memberId,
    						   @RequestParam("passwd") String passwd,
    						   HttpSession session){
    	
       MemberDto member = memberService.checkMember(memberId, passwd); 

       if (member != null) {
       session.setAttribute("loginuser", member);
        
        return "redirect:/";
        
       } else {
    	   return "auth/login";
       }
       
    }
    @GetMapping(path = {"/logout"})
    public String logout(HttpSession session) {
    	session.removeAttribute("loginuser");
    	return "redirect:login";
    }

	@GetMapping(path = { "/register" })
	public String register() {
		return "/auth/register";
	}
	
	@PostMapping("/register")
    public String registerProcess(@ModelAttribute MemberDto member) {
        	
		memberService.insertMember(member);
        // 성공 시 리다이렉트 (임시로 홈으로 이동)
        return "redirect:/";
    }
	
	@GetMapping(path = {"myPage"} )
	public String myPage() {
		return "auth/myPage";
	}
	
}