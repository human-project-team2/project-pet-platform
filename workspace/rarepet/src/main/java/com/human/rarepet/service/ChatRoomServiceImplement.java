package com.human.rarepet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.human.rarepet.dto.ChatMessageDto;
import com.human.rarepet.mapper.ChatRoomMapper;

@Service
public class ChatRoomServiceImplement implements ChatRoomService {

	private final ChatRoomMapper chatRoomMapper;
	
	public ChatRoomServiceImplement(ChatRoomMapper chatRoomMapper) {
		this.chatRoomMapper = chatRoomMapper;
	}
	
	@Override
	public List<ChatMessageDto> getChatMessagesByChatRoomId(String chatRoomId) {
		return chatRoomMapper.getChatMessagesByChatRoomId(chatRoomId);
	}

	@Override
	public void insertChatMessage(ChatMessageDto chatMessage) {
		// 채팅 메시지를 데이터베이스에 저장하는 로직
		chatRoomMapper.insertChatMessage(chatMessage);
		
	}

}
