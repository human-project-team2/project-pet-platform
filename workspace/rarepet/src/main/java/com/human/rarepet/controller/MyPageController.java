package com.human.rarepet.controller;

import java.io.File;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.human.rarepet.common.Util;
import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.dto.ShopDto;
import com.human.rarepet.security.RarepetUserDetails;
import com.human.rarepet.service.MarketService;
import com.human.rarepet.service.MemberService;
import com.human.rarepet.service.ShopService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MyPageController {

    
    private MemberService memberService;
    private MarketService marketService;
    private ShopService shopService;
    
    public MyPageController(MemberService memberService, MarketService marketService, ShopService shopService) {
    	this.memberService = memberService;
    	this.marketService = marketService;
    	this.shopService = shopService;
    }
    @GetMapping("/mypage")
    public String myPage(@AuthenticationPrincipal RarepetUserDetails userDetails , Model model) {
       
    	// 1. 세션에서 로그인한 유저 정보 가져오기 (소문자 loginuser 확인)
    	// -> Spring Security 로 바꿈.
        MemberDto loginUser = userDetails.getMember();

        // 2. 로그인 안 되어 있으면 로그인 페이지로 강제 이동
        if (loginUser == null) {
            return "redirect:/auth/login";
        }

        // 3. 서비스 호출: DB에서 유저 정보와 해당 유저의 상품 리스트 가져오기
        MemberDto user = memberService.getUserById(loginUser.getId());
        List<ProductDto> myProducts = marketService.findProductsByUserId(loginUser.getId());

        ShopDto shop = (ShopDto)shopService.showShopInfo(user.getId());
        
        // 4. 화면(HTML)으로 데이터 전달
        
        model.addAttribute("user", user);
        model.addAttribute("myProducts", myProducts);
        model.addAttribute("productCount", myProducts.size());
        model.addAttribute("shop", shop);
        // templates/auth/myPage.html 파일을 찾아갑니다.
        return "auth/myPage";
    }
    
	// 1. 수정 페이지 열기
	@GetMapping("/editProfile")
	public String editProfile(@AuthenticationPrincipal RarepetUserDetails userDetails,
							Model model) {
		
	    MemberDto loginUser = userDetails.getMember();
	    if (loginUser == null) return "redirect:/auth/login";

	    // 최신 정보를 DB에서 다시 가져와서 모델에 담기
	    model.addAttribute("user", memberService.getUserById(loginUser.getId()));
	    return "auth/editProfile"; // templates/auth/editProfile.html
	}

	final String UPLOAD_PATH = "d:\\uploaded_images";
	
	// 2. 수정 실행
	@PostMapping("/updateProfile")
	public String updateProfile(MemberDto member,
								@RequestParam("picture") MultipartFile picture) {
		
		if(picture != null && !picture.isEmpty()) {
			
			String savedFileName = Util.makeUniqueFileName(picture.getOriginalFilename());
			File file = new File(UPLOAD_PATH, savedFileName);
			String imgUrl = "/images/" + savedFileName;
			
			try {
				member.setImgUrl(imgUrl);
				member.setOriginalFileName(picture.getOriginalFilename());
				member.setSavedFileName(savedFileName);
				picture.transferTo(file);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
	    // 1. DB 업데이트
	    memberService.updateMember(member);
	    
	    // Spring Security로 로그인한 유저 정보 가져오기 로 대체
	    // 2. [추천] DB에서 최신 정보를 다시 가져와서 세션에 저장
	    // 이렇게 하면 폼에 없던 다른 정보들까지 누락 없이 세션에 유지됩니다.
	    //MemberDto updatedUser = memberService.getUserById(member.getId());
	    //session.setAttribute("loginuser", updatedUser);
	    
	    
	    
	    return "redirect:/mypage";
	}
}