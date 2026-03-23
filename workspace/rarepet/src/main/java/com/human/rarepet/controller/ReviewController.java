package com.human.rarepet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
		if (userDetails == null) {
			return "redirect:/auth/login";
		}
		
		model.addAttribute("list", reviewService.getReviewList());
		return "review/list";
	}

	// [기존 write 메서드 수정] 파라미터를 추가하여 상품 정보를 받습니다.
	@GetMapping(path = { "/write" })
	public String write(
			@RequestParam(value = "productName", required = false) String productName,
			@RequestParam(value = "productNo", required = false) Integer productNo,
			@RequestParam(value = "orderNo", required = false) Integer orderNo,
			Model model) {
		
		// 화면(Thymeleaf)으로 상품명과 번호들을 전달합니다.
		model.addAttribute("productName", productName);
		model.addAttribute("productNo", productNo);
		model.addAttribute("orderNo", orderNo);
		
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
		ReviewDto review = reviewService.getReviewByNo(reviewNo);
		model.addAttribute("review", review);
		return "review/edit";
	}

	// 2. 리뷰 수정 실행 처리
	@PostMapping("/edit")
	public String updateReview(ReviewDto review) {
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
	public String reviewWriteForm(@RequestParam("productNo") Long productNo,
	                             @RequestParam("orderNo") String orderNo,
	                             @RequestParam("productName") String productName,
	                             Model model) {
	    
	    model.addAttribute("productNo", productNo);
	    model.addAttribute("orderNo", orderNo);
	    model.addAttribute("productName", productName); // 상품명을 모델에 담음
	    
	    return "review/write_form"; // 리뷰 작성 페이지 경로
	}
}