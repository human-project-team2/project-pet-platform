package com.human.rarepet.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.MessageDto;
import com.human.rarepet.mapper.MessageMapper;
import com.human.rarepet.security.RarepetUserDetails;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service("messageService")
public class MessageService {

	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private MessageMapper messageMapper;
	
	public void sendMessage(MessageDto message, @AuthenticationPrincipal RarepetUserDetails userDetails ) throws RuntimeException {
		
		MemberDto member = userDetails.getMember();
		message.setSentAt(new Date());
		message.setSenderId(member.getId());
		
		messageMapper.sendMessage(message);
		
		if(message.getReceiverId() != null) {
			notificationService.sendToUser(message.getReceiverId(), message.getReceiverId() + "님으로 부터 메시지");
			
		}
	}
	
	public List<MessageDto> receiveMessage(String loggedInId){
		
		return messageMapper.receiveMessage(loggedInId);
	}
	
	public List<MessageDto> messageDetail(String loggedInId, String partnerId){
		return messageMapper.messageDetail(loggedInId, partnerId);
	}
}
