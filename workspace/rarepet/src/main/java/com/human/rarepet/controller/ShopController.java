package com.human.rarepet.controller;

import java.io.File;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.human.rarepet.common.Util;
import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.ShopDto;
import com.human.rarepet.security.RarepetUserDetails;
import com.human.rarepet.service.ShopService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShopController {

	final String UPLOAD_PATH = "d:\\uploaded_images";
	private ShopService shopService;
	public ShopController(ShopService shopService) {
		this.shopService = shopService;
	}
	
	@GetMapping(path = {"/shop-register"})
	public String registerShopPage(Model model) {
		model.addAttribute("shop", new ShopDto());
		return "shop/shop-register";
	}
	
	@PostMapping(path = {"/shop-register"})
	public String insertShop(ShopDto shop, 
							@AuthenticationPrincipal RarepetUserDetails userDetails, 
							@RequestParam("picture") MultipartFile picture) {
		
		String id = userDetails.getUsername();
		if(picture != null && picture.getOriginalFilename().length() > 0) {
			String savedFileName = Util.makeUniqueFileName(picture.getOriginalFilename());
			File file = new File(UPLOAD_PATH, savedFileName);
			String imgUrlShop = "/images/" + savedFileName;
			
			try {
				
				shop.setOriginalFileName(picture.getOriginalFilename());
				shop.setSavedFileName(savedFileName);
				shop.setImgUrlShop(imgUrlShop);
				picture.transferTo(file);
				shopService.insertShop(shop, id);
			} catch (Exception x) {
				x.printStackTrace();
			}
			
		}
		
		return "redirect:/shop/shopmain";
	}
	
	@GetMapping(path = {"/shop/shopmain"})
	public String shopPage(@AuthenticationPrincipal RarepetUserDetails userDetails, Model model) {
		MemberDto member = userDetails.getMember();
		String id = member.getId();
		model.addAttribute("shop", shopService.showShopInfo(id)); 
		return "shop/shopmain";
	}
	
	@GetMapping(path = {"/shop/editshop"})
	public String shopEdit(@AuthenticationPrincipal RarepetUserDetails userDetails, Model model) {
		MemberDto member = userDetails.getMember();
		String id = member.getId();
		model.addAttribute("shop", shopService.showShopInfo(id)); 
		return "shop/editshop";
	}
	
	@PostMapping(path = {"/shop/editshop"})
	public String updateShop(ShopDto shop, 
			@AuthenticationPrincipal RarepetUserDetails userDetails, 
			@RequestParam("picture") MultipartFile picture,
			@RequestParam("existingOriginalFileName") String existingOriginalFileName,
			@RequestParam("existingSavedFileName") String existingSavedFileName,
			@RequestParam("existingImgUrlShop") String existingImgUrlShop) {

		MemberDto member = userDetails.getMember();
		String id = member.getId();
		
		if (picture != null && picture.getOriginalFilename().length() > 0) {
			String savedFileName = Util.makeUniqueFileName(picture.getOriginalFilename());
			File file = new File(UPLOAD_PATH, savedFileName);
			String imgUrlShop = "/images/" + savedFileName;

			try {
				shop.setId(id);
				shop.setOriginalFileName(picture.getOriginalFilename());
				shop.setSavedFileName(savedFileName);
				shop.setImgUrlShop(imgUrlShop);
				picture.transferTo(file);
			
				shopService.updateShop(shop);
				
			} catch (Exception x) {
				x.printStackTrace();
			}

		} else {
			shop.setId(id);
			shop.setOriginalFileName(existingOriginalFileName);
			shop.setSavedFileName(existingSavedFileName);
			shop.setImgUrlShop(existingImgUrlShop);
			shopService.updateShop(shop);
		}

		return "redirect:/shop/shopmain";
	}
	
}
