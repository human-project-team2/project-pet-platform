package com.human.rarepet.controller;

import java.io.File;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.human.rarepet.common.Util;
import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.ShopDto;
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
	public String insertShop(ShopDto shop, HttpSession session, @RequestParam("picture") MultipartFile picture) {
		
		if(picture != null && picture.getOriginalFilename().length() > 0) {
			String savedFileName = Util.makeUniqueFileName(picture.getOriginalFilename());
			File file = new File(UPLOAD_PATH, savedFileName);
			String imgUrlShop = "/images/" + savedFileName;
			
			try {
				
				shop.setOriginalFileName(picture.getOriginalFilename());
				shop.setSavedFileName(savedFileName);
				shop.setImgUrlShop(imgUrlShop);
				picture.transferTo(file);
				shopService.insertShop(shop, session);
			} catch (Exception x) {
				x.printStackTrace();
			}
			
		}
		
		return "redirect:/shop/shopmain";
	}
	
	@GetMapping(path = {"/shop/shopmain"})
	public String shopPage(HttpSession session, Model model) {
		MemberDto member = (MemberDto)session.getAttribute("loginuser");
		String id = member.getId();
		model.addAttribute("shop", shopService.showShopInfo(id)); 
		return "shop/shopmain";
	}
	
	@GetMapping(path = {"/shop/editshop"})
	public String shopEdit(HttpSession session, Model model) {
		MemberDto member = (MemberDto)session.getAttribute("loginuser");
		String id = member.getId();
		model.addAttribute("shop", shopService.showShopInfo(id)); 
		return "shop/editshop";
	}
	
	@PostMapping(path = {"/shop/editshop"})
	public String updateShop(ShopDto shop, HttpSession session, @RequestParam("picture") MultipartFile picture) {

		if (picture != null && picture.getOriginalFilename().length() > 0) {
			String savedFileName = Util.makeUniqueFileName(picture.getOriginalFilename());
			File file = new File(UPLOAD_PATH, savedFileName);
			String imgUrlShop = "/images/" + savedFileName;

			try {

				shop.setOriginalFileName(picture.getOriginalFilename());
				shop.setSavedFileName(savedFileName);
				shop.setImgUrlShop(imgUrlShop);
				picture.transferTo(file);
				
				shopService.selectiveUpdateShop(shop, session);
				
			} catch (Exception x) {
				x.printStackTrace();
			}

		}

		return "redirect:/shop/shopmain";
	}
	
}
