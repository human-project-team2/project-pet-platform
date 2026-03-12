package com.human.rarepet.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BoardAdoptionDto extends AdoBoardDto {
	
	private int boardId;   		// FK
	private String id; 		// FK	
	private String adoptApplyMessage;

}