package com.human.rarepet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
	public String review(@AuthenticationPrincipal RarepetUserDetails userDetails, Model model) {
		// 목록은 비로그인 사용자도 볼 수 있게 하려면 아래 null 체크를 주석 처리하세요.
		// 현재는 로그인 필수 설정입니다.
		if (userDetails == null) {
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
		reviewService.registerReview(review);
		return "redirect:/review/list";
	}

	// --- 아래 기능을 추가했습니다 ---

	// 1. 리뷰 수정 페이지 이동
	@GetMapping("/edit/{no}")
	public String editForm(@PathVariable("no") Long reviewNo, Model model) {
		// 해당 번호의 리뷰 상세 정보를 가져와서 모델에 담기
		ReviewDto review = reviewService.getReviewByNo(reviewNo);
		model.addAttribute("review", review);
		return "review/edit"; // review/edit.html 페이지가 필요합니다.
	}

	// 2. 리뷰 수정 실행 처리
	@PostMapping("/edit")
	public String updateReview(ReviewDto review) {
		// reviewDto에 담긴 reviewNo를 기준으로 내용을 업데이트
		reviewService.modifyReview(review);
		return "redirect:/review/list";
	}

	// 3. 리뷰 삭제 처리
	@GetMapping("/delete/{no}")
	public String deleteReview(@PathVariable("no") Long reviewNo) {
		reviewService.removeReview(reviewNo);
		return "redirect:/review/list";
	}
	
	@GetMapping("/review/write")
	public String writeReview(@RequestParam("productName") String productName, Model model) {
	    model.addAttribute("productName", productName);
	    return "review/write"; 
	}
	
	
}