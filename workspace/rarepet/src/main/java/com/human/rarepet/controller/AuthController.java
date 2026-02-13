package com.human.rarepet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.human.rarepet.dto.AuthDto;
import com.human.rarepet.dto.ProductRegisterDto;

@Controller
@RequestMapping("/auth")
public class AuthController {

	@GetMapping(path = { "/login" })
	public String login() {

		return "/auth/login";
	}
    @PostMapping("/login")
    public String loginProcess(@ModelAttribute AuthDto dto) {
        
        // 데이터 수신 확인을 위한 콘솔 출력
        System.out.println("===== 상품 등록 데이터 수신 확인 =====");
        System.out.println("id: " + dto.getMemberId());
        System.out.println("pw: " + dto.getPasswd());
		System.out.println("==================================");

        // 성공 시 리다이렉트 (임시로 홈으로 이동)
        return "/home";
    }

	@GetMapping(path = { "/register" })
	public String register() {
		return "/auth/register";
	}
	
	@PostMapping("/register")
    public String registerProcess(@ModelAttribute AuthDto dto) {
        
        // 데이터 수신 확인을 위한 콘솔 출력
        System.out.println("===== 상품 등록 데이터 수신 확인 =====");
        System.out.println("id: " + dto.getMemberId());
        System.out.println("pw: " + dto.getPasswd());
        System.out.println("pw: " + dto.getConfirm());
		System.out.println("==================================");

        // 성공 시 리다이렉트 (임시로 홈으로 이동)
        return "/home";
    }

	
}