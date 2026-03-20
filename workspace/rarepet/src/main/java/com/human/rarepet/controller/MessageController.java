package com.human.rarepet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.MessageDto;
import com.human.rarepet.security.RarepetUserDetails;
import com.human.rarepet.service.MessageService;
import com.human.rarepet.service.NotificationService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MessageController {
	
	private final MessageService messageService;
	
	
	public MessageController(MessageService messageService, NotificationService notificationService) {
		
		this.messageService = messageService;
	}
	
	@PostMapping(path = {"/messages"})
	@ResponseBody
	public String sendMessage(@RequestBody MessageDto message, @AuthenticationPrincipal RarepetUserDetails userDetails) {
		try {
			messageService.sendMessage(message, userDetails);
			return "success";
		} catch (RuntimeException exception) {
			return "failure";
		}
	}
	
	@GetMapping(path = {"/messages/list"})
	@ResponseBody
	public List<MessageDto> receiveMessage(@AuthenticationPrincipal RarepetUserDetails userDetails) {
		
		MemberDto member = userDetails.getMember();
		String loggedInId = member.getId();
		return messageService.receiveMessage(loggedInId);
	}
	
	@GetMapping(path = {"/messages/detail"})
	@ResponseBody
	public List<MessageDto> messageDetail(@RequestParam("partnerId") String partnerId, 
							@AuthenticationPrincipal RarepetUserDetails userDetails){
		
		MemberDto member = userDetails.getMember();
		String loggedInId = member.getId();
		return messageService.messageDetail(loggedInId, partnerId);
		
		
	}
	
}
