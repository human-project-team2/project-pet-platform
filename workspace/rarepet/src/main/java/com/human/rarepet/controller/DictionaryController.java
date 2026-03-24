package com.human.rarepet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DictionaryController {

	@GetMapping(path= {"/dictionary"})
	public String showDictionaryPage() {
		return "dictionary";
	}
}
