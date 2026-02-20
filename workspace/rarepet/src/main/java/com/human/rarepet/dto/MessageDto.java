package com.human.rarepet.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MessageDto {

	int messageNo;
	String messageContent;
	String receiverId;
	String senderId;
	Date sendDate;
	Date receiveDate;
	
}
