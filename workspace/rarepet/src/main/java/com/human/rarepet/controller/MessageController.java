package com.human.rarepet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.MessageDto;
import com.human.rarepet.service.MessageService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MessageController {
	
	@Autowired
	private MessageService messageService;
	
	@PostMapping(path = {"/messages"})
	@ResponseBody
	public String sendMessage(@RequestBody MessageDto message, HttpSession session) {
		try {
			messageService.sendMessage(message, session);
			return "success";
		} catch (RuntimeException exception) {
			return "failure";
		}
	}
	
	@GetMapping(path = {"/messages/list"})
	@ResponseBody
	public List<MessageDto> receiveMessage(HttpSession session) {
		MemberDto member = (MemberDto) session.getAttribute("loginuser");
		String receiverId = member.getId();
		return messageService.receiveMessage(receiverId);
	}
	
	@GetMapping(path = {"/message/detail"})
	@ResponseBody
	public List<MessageDto> messageDetail(@RequestParam String senderId,
										  @RequestParam String receiverId){
		
		return messageService.messageDetail(senderId, receiverId);
	}
	
}
