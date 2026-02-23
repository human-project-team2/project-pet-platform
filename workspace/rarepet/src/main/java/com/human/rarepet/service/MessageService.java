package com.human.rarepet.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.human.rarepet.dto.MessageDto;
import com.human.rarepet.mapper.MessageMapper;

@Service("messageService")
public class MessageService {

	@Autowired
	private MessageMapper messageMapper;
	
	public void sendMessage(MessageDto message) {
		
		// 임시로 senderId
		//message.setSenderId("heh");
		message.setSendDate(new Date());
		messageMapper.sendMessage(message);
	}
	
	public List<MessageDto> receiveMessage(String receiverId){
		
		return messageMapper.receiveMessage(receiverId);
	}
}
