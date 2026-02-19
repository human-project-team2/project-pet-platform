package com.human.rarepet.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class BoardDto {

	private int boardNo;
	private String title;
	private String content;	
	private String writer;
	private int boardCount;
	private int boardRecCount;
	private Date boardModDate;
}
