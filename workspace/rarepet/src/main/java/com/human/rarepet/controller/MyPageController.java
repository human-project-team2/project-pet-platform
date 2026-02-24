package com.human.rarepet.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.service.MarketService;
import com.human.rarepet.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MyPageController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MarketService marketService;

    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {
        // 1. 세션에서 로그인한 유저 정보 가져오기 (소문자 loginuser 확인)
        MemberDto loginUser = (MemberDto) session.getAttribute("loginuser");

        // 2. 로그인 안 되어 있으면 로그인 페이지로 강제 이동
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        // 3. 서비스 호출: DB에서 유저 정보와 해당 유저의 상품 리스트 가져오기
        MemberDto user = memberService.getUserById(loginUser.getId());
        List<ProductDto> myProducts = marketService.findProductsByUserId(loginUser.getId());

        // 4. 화면(HTML)으로 데이터 전달
        model.addAttribute("user", user);
        model.addAttribute("myProducts", myProducts);
        model.addAttribute("productCount", myProducts.size());

        // templates/auth/myPage.html 파일을 찾아갑니다.
        return "auth/myPage";
    }
    
	// 1. 수정 페이지 열기
	@GetMapping("/editProfile")
	public String editProfile(HttpSession session, Model model) {
	    MemberDto loginUser = (MemberDto) session.getAttribute("loginuser");
	    if (loginUser == null) return "redirect:/auth/login";

	    // 최신 정보를 DB에서 다시 가져와서 모델에 담기
	    model.addAttribute("user", memberService.getUserById(loginUser.getId()));
	    return "auth/editProfile"; // templates/auth/editProfile.html
	}

	// 2. 수정 실행
	@PostMapping("/updateProfile")
	public String updateProfile(MemberDto member, HttpSession session) {
	    // 1. DB 업데이트
	    memberService.updateMember(member);
	    
	    // 2. [추천] DB에서 최신 정보를 다시 가져와서 세션에 저장
	    // 이렇게 하면 폼에 없던 다른 정보들까지 누락 없이 세션에 유지됩니다.
	    MemberDto updatedUser = memberService.getUserById(member.getId());
	    session.setAttribute("loginuser", updatedUser);
	    
	    return "redirect:/mypage";
	}
}