package com.human.rarepet.dto;

import lombok.Data;

@Data
public class ChatMessageDto {

	private String chatRoomId;
	private String sender;
	private String content;
	private String timestamp;
}
