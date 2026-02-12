package com.human.rarepet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductRegisterContorller {

	@GetMapping(path = {"/productRegister"})
	public String product() {
		
		return "productRegister"; // /resources/templates/ + home + .html
	}
}
