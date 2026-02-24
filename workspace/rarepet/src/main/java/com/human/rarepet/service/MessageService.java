package com.human.rarepet.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.dto.MessageDto;
import com.human.rarepet.mapper.MessageMapper;

import jakarta.servlet.http.HttpSession;

@Service("messageService")
public class MessageService {

	@Autowired
	private MessageMapper messageMapper;
	
	public void sendMessage(MessageDto message, HttpSession session) throws RuntimeException {
		
		MemberDto member = (MemberDto)session.getAttribute("loginuser");
		message.setSentAt(new Date());
		message.setSenderId(member.getId());
		
		messageMapper.sendMessage(message);
	}
	
	public List<MessageDto> receiveMessage(String receiverId){
		
		return messageMapper.receiveMessage(receiverId);
	}
	
	public List<MessageDto> messageDetail(String senderId, String receiverId){
		return messageMapper.messageDetail(senderId, receiverId);
	}
}
