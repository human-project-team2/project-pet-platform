package com.human.rarepet.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.service.AdminService;

@RestController
public class TestController {

	private final AdminService adminService;
	public TestController(AdminService adminService) {
		this.adminService = adminService;
	}
	
	
	@GetMapping(path = {"/react-test"})
	// @ResponseBody
	public List<MemberDto> reactTest() {
		return adminService.getAllMembers();
	}
}
