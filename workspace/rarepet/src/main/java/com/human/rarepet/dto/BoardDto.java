package com.human.rarepet.dto;

import java.sql.Date;

//import com.human.rarepet.dto.MemberDto;

import lombok.Data;

@Data
public class BoardDto {

	private int boardId;
	private String boardTitle;
	private String boardContent;	
	private String boardWriterId;
	private int boardLikeCount;
	private int boardViewCount;
	private java.util.Date boardWriteDate;
	private String savedFileName;
	private String originalFileName;
	private String imgUrl;
	
	//private String email;
	//private MemberDto writer;
}
