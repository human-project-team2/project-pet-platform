package com.human.rarepet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.service.MarketService;

@Controller
public class HomeContoroller {
	
	@Autowired
	private MarketService marketService;
	
	@GetMapping(path = {"/","/home"})
	public String home(Model model) {
		List<ProductDto> products = marketService.showAllProduct();
		
		model.addAttribute("products", products);
		return "home"; // /resources/templates/ + home + .html
		
	}
}
