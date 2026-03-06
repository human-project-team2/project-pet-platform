package com.human.rarepet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.ReviewDto;
import com.human.rarepet.security.RarepetUserDetails;
import com.human.rarepet.service.ReviewService;

@Controller
@RequestMapping("/review")
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;


	@GetMapping(path = { "/list" })
	public String review(@AuthenticationPrincipal RarepetUserDetails userDetails , Model model) {
		
		if (userDetails == null) {
			return "redirect:/auth/login";
		}
		// 1. 세션에서 로그인한 유저 정보 가져오기 (소문자 loginuser 확인)
		MemberDto loginUser = userDetails.getMember();

		// 2. 로그인 안 되어 있으면 로그인 페이지로 강제 이동
		if (loginUser == null) {
			return "redirect:/auth/login";
		}
		
		model.addAttribute("list", reviewService.getReviewList());
		return "review/list";
	}

	@GetMapping(path = { "/write" })
	public String write() {
		return "review/write";
	}

	
	// 리뷰 등록 처리
	@PostMapping("/write")
	public String writeForm(ReviewDto review) {
		System.out.println(review);
		reviewService.registerReview(review);
		return "redirect:/review/list"; // 등록 후 목록으로 이동
	}

	

}
