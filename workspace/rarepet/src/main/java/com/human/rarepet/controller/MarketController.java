package com.human.rarepet.controller;



import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.human.rarepet.common.Util;
import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.dto.ShoppingCartDto;
import com.human.rarepet.dto.UpdateCartQuantityDto;
import com.human.rarepet.service.MarketService;

import jakarta.servlet.http.HttpSession;


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
	
	@GetMapping(path="/market/detail/{productNo}")
	public String marketDetail(@PathVariable("productNo") int productNo, Model model) {
		ProductDto product = marketService.selectProductByNo(productNo);
		model.addAttribute("product", product);
		return "market/detail";
	}
	
	@GetMapping(path= {"/market/register"})
	public String register() {
		return "market/register";
	}
	
	final String UPLOAD_PATH = "d:\\uploaded_images";
	
	@PostMapping(path = {"/market/register"})
	public String registerProduct(ProductDto product, @RequestParam("picture") MultipartFile picture,
								  HttpSession session) {
				
			MemberDto member = (MemberDto)session.getAttribute("loginuser");
			
			if (picture != null && picture.getOriginalFilename().length() > 0) {
				
				String savedFileName = Util.makeUniqueFileName(picture.getOriginalFilename());
				
				File file = new File(UPLOAD_PATH, savedFileName);
				String imgUrl = "/images/" + savedFileName;
				
				try {
					
					String id = member.getId();
					product.setSellerId(id);
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
	
	@GetMapping(path = {"/market/shoppingCart"})
	public String shoppingCart(Model model, HttpSession session) {
		
		MemberDto member = (MemberDto)session.getAttribute("loginuser");
		if(member == null) {
			return "redirect:/auth/login";
		} 
		String id = member.getId();
		
		List<ShoppingCartDto> cartList = marketService.selectShoppingCart(id);
		
		model.addAttribute("cartList", cartList);
		return "market/shoppingCart";
	}
	
	@PostMapping(path = {"/market/shoppingCart"})
	public String addShoppingCart(@RequestParam("productNo") int productNo, 
									HttpSession session,
									RedirectAttributes redirectAttributes) {
		
		MemberDto member = (MemberDto)session.getAttribute("loginuser");
		if(member == null) {
			return "redirect:/auth/login";
		} 
		String id = member.getId();

		try {
			marketService.addShoppingCart(productNo, id);
			redirectAttributes.addFlashAttribute("message", "장바구니에 담겼습니다.");
			
		} catch (DuplicateKeyException exception) {
			redirectAttributes.addFlashAttribute("message", "이미 장바구니에 담긴 상품입니다");
			
		}
		
		return "redirect:/market/shoppingCart";
	}
	
	@GetMapping(path = {"/market/deleteShoppingCart"})
	public String deleteShoppingCart(@RequestParam("productNo") int productNo, HttpSession session) {
		
		MemberDto member = (MemberDto) session.getAttribute("loginuser");
		String id = member.getId();
		marketService.deleteShoppingCart(productNo, id);
		return "redirect:/market/shoppingCart";
	}
	
	@PostMapping(path = {"/market/updateShoppingCart"})
	@ResponseBody
	public String updateShoppingCart(@RequestBody List<UpdateCartQuantityDto> updatedList,
									 HttpSession session,
									 RedirectAttributes redirectAttributes) {
		
		MemberDto member = (MemberDto) session.getAttribute("loginuser");

		if(member != null) {
			String id = member.getId();
			marketService.updateShoppingCartList(id, updatedList);
			return "success";
		} else {
			return "failure";
		}
	}
}
