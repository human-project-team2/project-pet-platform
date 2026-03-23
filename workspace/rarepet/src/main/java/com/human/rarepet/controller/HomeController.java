package com.human.rarepet.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.human.rarepet.dto.BoardDto;
import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.service.BoardService;
import com.human.rarepet.service.MarketService;

@Controller
public class HomeController {
	
	@Autowired
	private MarketService marketService;

    // ✨ 1. BoardService를 스프링으로부터 주입받아야 합니다. (이게 빠져있었습니다!)
    @Autowired
    private BoardService boardService;
	
	@GetMapping(path = {"/", "/home"})
	public String home(
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "category", required = false, defaultValue = "all") String category,
			Model model) {
		
		// 서비스에서 필터링된 최신 상품 리스트를 가져옵니다.
		List<ProductDto> latestProducts = marketService.searchProducts(search, category);
		
		// HTML에서 사용할 이름 'latestProducts'로 모델에 담습니다.
		model.addAttribute("latestProducts", latestProducts);
		
		// 검색 상태 유지용 데이터
		model.addAttribute("search", search);
		model.addAttribute("category", category);
		
		// ✨ 2. 대문자 BoardService가 아니라 주입받은 소문자 boardService를 사용합니다.
		List<BoardDto> dailyTrending = boardService.getTrendingBoards(1);
        model.addAttribute("dailyTrending", dailyTrending);
		
		return "home";
	}
	
	@GetMapping("/api/trending")
	@ResponseBody // 데이터를 JSON 형태로 반환합니다.
	public List<BoardDto> getTrendingApi(@RequestParam(value = "days", defaultValue = "1") int days) {
	    // 서비스에서 해당 기간의 데이터를 가져와 반환합니다.
	    return boardService.getTrendingBoards(days);
	}
}