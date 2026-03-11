package com.human.rarepet.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.human.rarepet.dto.ChatMessageDto;

@Controller
public class ChatRoomController {

	@GetMapping(path = {"/chatRoom"})
	public String chatRoomTest() {
		return "chatRoom";
	}
	
	@MessageMapping("/sendWsMessage")
	@SendTo("/topic/chatRoom")
	public ChatMessageDto sendWsMessage(ChatMessageDto message) {
		
		return message;
	}
}
