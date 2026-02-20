package com.human.rarepet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.human.rarepet.dto.MessageDto;
import com.human.rarepet.service.MessageService;

@Controller
public class MessageController {
	
	@Autowired
	private MessageService messageService;
	
	@PostMapping(path = {"/messages"})
	public void sendMessage(@RequestBody MessageDto message) {
		System.out.println(message);
		messageService.sendMessage(message);
	}
}
