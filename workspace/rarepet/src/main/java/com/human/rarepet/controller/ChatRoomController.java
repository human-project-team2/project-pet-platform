package com.human.rarepet.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.human.rarepet.dto.ChatMessageDto;
import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.security.RarepetUserDetails;
import com.human.rarepet.service.ChatRoomService;

@Controller
public class ChatRoomController {

	private final ChatRoomService chatRoomService;
	private final SimpMessagingTemplate messagingTemplate;
	

	public ChatRoomController(ChatRoomService chatRoomService, 
			                  SimpMessagingTemplate messagingTemplate) {
		this.chatRoomService = chatRoomService;
		this.messagingTemplate = messagingTemplate;
	}
	
	@GetMapping(path = {"/chatRoom/{chatRoomId}"})
	@ResponseBody
	public List<ChatMessageDto> chatRoomTest(@AuthenticationPrincipal RarepetUserDetails userDetails,
								@PathVariable(value = "chatRoomId") String chatRoomId,
								Model model ) {
		if (userDetails != null) {
			
			MemberDto member = userDetails.getMember();
			model.addAttribute("member", member);
			
			return chatRoomService.getChatMessagesByChatRoomId(chatRoomId);
			
		} else {
			return List.of(); // 인증되지 않은 사용자는 빈 리스트 반환
		}
		
	}
	
	@MessageMapping("/sendWsMessage/{chatRoomId}")
	public void sendWsMessage(@DestinationVariable("chatRoomId") String chatRoomId, // websocket 에서는 @PathVariable 대신 @DestinationVariable 사용
										ChatMessageDto message) {
		 messagingTemplate.convertAndSend("/topic/chatRoom/" + chatRoomId, message);
		 chatRoomService.insertChatMessage(message); // 메시지를 데이터베이스에 저장
		// messagingTemplate.convertAndSend : 메시지를 WebSocket을 통해 해당 채팅방에게만 전송
		// return message; -> 이렇게 하면 클라이언트로 메시지를 반환하여 실시간 업데이트, 위와 같은 다른 방식으로 해야함.
			
	}
	
	
}
