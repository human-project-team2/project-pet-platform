package com.human.rarepet.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.service.MarketService;



@Controller
public class MarketController {
	
	@Autowired
	private MarketService marketService;
	
	@GetMapping(path={"/market"})
	public String market(Model model) {
	
		List<ProductDto> products = marketService.showAllProduct();
				
		model.addAttribute("products", products);
		return "market/list";
	}
	
	@GetMapping(path="/market/detail")
	public String marketDetail() {
		return "market/detail";
	}
	
	@GetMapping(path= {"/market/register"})
	public String register() {
		return "market/register";
	}
	
	@PostMapping(path = {"/market/register"})
	public String registerProduct(ProductDto product) {
		
		marketService.registerProduct(product);
		return "redirect:/market";
	}
	
	
}
