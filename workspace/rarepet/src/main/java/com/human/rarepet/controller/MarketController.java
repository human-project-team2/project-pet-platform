package com.human.rarepet.controller;



import java.io.File;
import java.util.List;

import org.apache.tomcat.util.modeler.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	
	final String UPLOAD_PATH = "d:\\uploaded_images";
	
	@PostMapping(path = {"/market/register"})
	public String registerProduct(ProductDto product, @RequestParam("picture") MultipartFile picture) {
				
		
			if (picture != null && picture.getOriginalFilename().length() > 0) {
				
				String savedFileName = com.human.rarepet.common.Util.makeUniqueFileName(picture.getOriginalFilename());
				
				File file = new File(UPLOAD_PATH, savedFileName);
				String imgUrl = "/images/" + savedFileName;
				
				try {
					
					product.setImgUrl(imgUrl);
					product.setOriginalFileName(picture.getOriginalFilename());
					product.setSavedFileName(savedFileName);
					marketService.registerProduct(product);
					picture.transferTo(file);
				}	catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		
		return "redirect:/market";
	}
	
	
}
