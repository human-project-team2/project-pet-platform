package com.human.rarepet.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ChatMessageDto {
	
	private int chatMessageNo;
	private String chatRoomId;
	private String sender;
	private String content;
	private Date timestamp;
}
