package com.human.rarepet.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.service.MarketService;

@Controller
public class HomeContoroller {
	
	@Autowired
	private MarketService marketService;
	
	@GetMapping(path = {"/","/home"})
	public String home(
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "category", required = false) String category,
			Model model) {
		
		// 검색 조건이 있으면 필터링된 리스트를, 없으면 전체 리스트를 가져옵니다.
		List<ProductDto> products = marketService.searchProducts(search, category);
		
		model.addAttribute("products", products);
		// 뷰에서 현재 검색 상태를 유지하기 위해 다시 넘겨줍니다.
		model.addAttribute("search", search);
		model.addAttribute("category", category);
		
		return "home";
	}
}