package com.human.rarepet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.human.rarepet.service.AdminService;

@Controller
public class AdminController {

	private final AdminService adminService;

	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}
	
	@GetMapping(path = {"/admin"})
	public String admin(Model model) {
		
		model.addAttribute("members", adminService.getAllMembers());
		model.addAttribute("shops", adminService.getAllShops());
		System.out.println(adminService.getAllMembers());
		return "admin";
	}
	
	@PostMapping(path = {"/admin/verify/{id}"})
	public String verify(@PathVariable("id") String id, Model model) {
		System.out.println("여기까지 왔음");
		return "admin/verify";
	}
	
	
}
