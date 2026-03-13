package com.human.rarepet.dto;

import java.util.Date;

import lombok.Data;

@Data
public class QnaBoardAnswerDto extends QnaBoardDto {
	
	private int answerId;   // PK
	private String id; 		// FK	
	private int boardId;   	// FK	
	private String content;

}