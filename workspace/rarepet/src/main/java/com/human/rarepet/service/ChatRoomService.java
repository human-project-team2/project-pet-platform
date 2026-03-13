package com.human.rarepet.service;

import java.util.List;

import com.human.rarepet.dto.ChatMessageDto;

public interface ChatRoomService {

	List<ChatMessageDto> getChatMessagesByChatRoomId(String chatRoomId);
	void insertChatMessage(ChatMessageDto chatMessage);
}
