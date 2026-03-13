package com.human.rarepet.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.ShopDto;
import com.human.rarepet.service.AdminService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

	private final AdminService adminService;

	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}
	
	@GetMapping(path = {"/admin"})
	public String admin(Model model,
				       @RequestParam(required = false, value = "memberKeyword") String memberKeyword,
				       @RequestParam(required = false, value = "shopKeyword") String shopKeyword) {
		List<MemberDto> members;
		List<ShopDto> shops;
		
		
		if (memberKeyword != null && !memberKeyword.isBlank()) {
			members = adminService.searchMembers(memberKeyword);
		} else {
			members = adminService.getAllMembers();
		}
		
		if (shopKeyword != null && !shopKeyword.isBlank()) {
			shops = adminService.searchShops(shopKeyword);
		} else {
			shops = adminService.getAllShops();
		}
		
		// stream : 데이터를 처리하기 위한 파이프라인 , filter 로 가공, 다시 리스트화
		shops = shops.stream()
		.filter(shop -> !shop.getId().equals("administrator"))
		.toList();
		members = members.stream()
		.filter(member -> !member.getId().equals("administrator"))
		.toList();
		
		model.addAttribute("members",members);
		model.addAttribute("shops", shops);
		
		return "admin";
	}
	
	@GetMapping(path = {"/admin/verify/{id}"})
	public String verify(@PathVariable("id") String id) {
		
		adminService.verifyShop(id);
		return "redirect:/admin";
	}
	
	
}
